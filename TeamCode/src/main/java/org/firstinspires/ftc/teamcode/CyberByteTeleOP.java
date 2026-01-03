package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

//import android.graphics.Color;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "CyberByte 1-2026", group = "Linear OpMode")
public class CyberByteTeleOP extends LinearOpMode {

    // Hardware
    DcMotor Fleft, Bleft, Fright, Bright, SpinDexer, Top_Launch, Bottom_Launch, Intake;
    //intake go brr
    Servo LeftKicker, RightKicker;
    //private NormalizedColorSensor color_sensor_v3;
    Limelight3A limelight;

    // Movement Variables
    double speedMultiplier = 0.75;

    // Spindexer Constants
    // Using 251 as an int to prevent "jitter" from decimal targets
    int TICKS_PER_CHAMBER = 251;
    // Half distance for the B button logic (approx 125 ticks)
    int HALF_CHAMBER = 125;
    int spinTargetPosition = 0;

    // PID Coefficients
    double Kp = 0.005;
    double Ki = 0.0;
    double Kd = 0.0001;
    double integralSum = 0;
    ElapsedTime pidTimer = new ElapsedTime();
    double lastError = 0;

    // State Tracking
    //String[] chamberContents = {"EMPTY", "EMPTY", "EMPTY"};
    //int currentIntakeChamberIndex = 0;
    int KICKER_OFFSET = 1;

    enum KickerState {READY, BUMPING}

    KickerState kickerState = KickerState.READY;
    ElapsedTime kickerTimer = new ElapsedTime();

    // Servo Positions
    double KICK_READY_POS = 0.25;
    double KICK_UP_POS = 0.5;
    double BUMP_DURATION = 150;

    /*
    // Color Logic
    float[] hsv_values = new float[3];

    private List<String> detected_colors_list = new ArrayList<>();
    boolean color_detected_lately = false;

     */

    // Toggle states

    boolean launcherOn = false;
    boolean lastLaunch = false;
    boolean last2A = false;
    boolean lastA = false;
    boolean lastB = false;
    boolean last2B = false;
    boolean lastX = false;
    boolean lastToggle = false; // For mode switching (Fast/Normal)
    boolean isFastMode = false;  // Fast NOT default

    // Halfway state tracker for B button logic
    boolean isHalfway = false;

    @Override
    public void runOpMode() {
        // Initialization
        Fleft = hardwareMap.get(DcMotor.class, "Fleft");
        Fright = hardwareMap.get(DcMotor.class, "Fright");
        Bleft = hardwareMap.get(DcMotor.class, "Bleft");
        Bright = hardwareMap.get(DcMotor.class, "Bright");

        Intake = hardwareMap.get(DcMotor.class, "Intake");
        SpinDexer = hardwareMap.get(DcMotor.class, "SpinDexer");
        Top_Launch = hardwareMap.get(DcMotor.class, "Top_Launch");
        Bottom_Launch = hardwareMap.get(DcMotor.class, "Bottom_Launch");

        LeftKicker = hardwareMap.get(Servo.class, "LK");
        RightKicker = hardwareMap.get(Servo.class, "RK");
        //color_sensor_v3 = hardwareMap.get(NormalizedColorSensor.class, "color_sensor_v3");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Directions
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reversed Spindexer so encoder and power directions match
        SpinDexer.setDirection(DcMotorSimple.Direction.REVERSE);
        SpinDexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpinDexer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        LeftKicker.setDirection(Servo.Direction.FORWARD);
        RightKicker.setDirection(Servo.Direction.REVERSE);

        Top_Launch.setDirection(DcMotorSimple.Direction.FORWARD);
        Bottom_Launch.setDirection(DcMotorSimple.Direction.REVERSE);

        limelight.start();

        // Limelight Pipeline
        limelight.pipelineSwitch(0);

        telemetry.addData("Status", "Initialized - Motor Directions Fixed");
        telemetry.update();

        waitForStart();
        pidTimer.reset();

        while (opModeIsActive()) {
            // --- DRIVE TRAIN LOGIC ---
            double y = -gamepad1.right_stick_y;
            double x = gamepad1.right_stick_x;
            double rot = gamepad1.left_stick_x;

            /*
            y = -gamepad1.right_stick_y;
            rot = gamepad1.left_stick_x;
            x = gamepad1.right_stick_x;
             */

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
            Fleft.setPower(((y + x + rot) / denominator) * speedMultiplier);
            Fright.setPower(((y - x - rot) / denominator) * speedMultiplier);
            Bleft.setPower(((y - x + rot) / denominator) * speedMultiplier);
            Bright.setPower(((y + x - rot) / denominator) * speedMultiplier);

            if (gamepad1.left_bumper) speedMultiplier = 0.5;
            else if (gamepad1.right_bumper) speedMultiplier = 1.0;
            else speedMultiplier = 0.75;

            if (gamepad2.dpad_down && gamepad2.right_bumper) {
                spinTargetPosition = 0;
            }

            // --- MODE TOGGLE LOGIC ---
            // D-Pad Up toggles between Fast and Normal modes
            if (gamepad1.dpad_up && !lastToggle) {
                isFastMode = !isFastMode;
            }
            lastToggle = gamepad1.dpad_up;

            // --- SPINDEXER TARGET LOGIC ---

            // Logic for A Button
            if (gamepad1.a && !lastA) {
                if (isHalfway) {
                    // If we are currently in the halfway/blocked state, go back
                    spinTargetPosition -= HALF_CHAMBER;
                    isHalfway = false;
                    //telemetry.addLine("The Mode is Set to Shoot. You may not use the SpinDexer right now");
                    //telemetry.update();
                } else {
                    // Normal behavior: Advance one full chamber
                    spinTargetPosition += TICKS_PER_CHAMBER;
                    //currentIntakeChamberIndex = (currentIntakeChamberIndex + 1) % 3;
                }
            } else {
                // SpinDexer.setPower(0); // REMOVED: Don't kill power here, let PID handle it
            }
            lastA = gamepad1.a;

            // Logic for B Button (Gamepad1)
            if (gamepad1.b && !lastB) {
                if (isHalfway) {
                    // If we are currently in the halfway/blocked state, go back
                    spinTargetPosition -= HALF_CHAMBER;
                    isHalfway = false;
                    //telemetry.addLine("The Mode is Set to Shoot. You may not use the SpinDexer right now");
                    //telemetry.update();
                } else {
                    // Normal behavior: Reverse one full chamber
                    spinTargetPosition -= TICKS_PER_CHAMBER;
                    //currentIntakeChamberIndex = (currentIntakeChamberIndex - 1) % 3;
                }
            } else {
                // SpinDexer.setPower(0); // REMOVED: Don't kill power here, let PID handle it
            }
            lastB = gamepad1.b;

            // Logic for B Button (Gamepad2)
            if (gamepad2.b && !last2B) {
                if (!isHalfway) {
                    // Go HALF the distance of A (Forward half chamber)
                    // This blocks the intake but leaves kicker open
                    spinTargetPosition += HALF_CHAMBER;
                    isHalfway = true;
                } else {
                    spinTargetPosition -= HALF_CHAMBER;
                    isHalfway = false;
                }
            }
            last2B = gamepad2.b;

            /*
            if (gamepad2.x && !lastX && isHalfway) {
                spinTargetPosition -= HALF_CHAMBER;
                isHalfway = false;
            }
            lastX = gamepad2.x;

             */

            if (gamepad2.a && !last2A) {
                spinTargetPosition -= TICKS_PER_CHAMBER;
                //currentIntakeChamberIndex = (currentIntakeChamberIndex - 1) % 3;
            }
            last2A = gamepad2.a;


            /*
            // Logic for X Button
            if (gamepad2.x && !lastX) {
                spinTargetPosition -= HALF_CHAMBER;
                isHalfway = false;
            } else {
                // Normal behavior: Advance one full chamber
                spinTargetPosition += TICKS_PER_CHAMBER;
                currentIntakeChamberIndex = (currentIntakeChamberIndex + 1) % 3;
            }



             */


            if (gamepad2.dpad_left) {
                SpinDexer.setPower(0.25);
                spinTargetPosition = 0;
                isHalfway = false; // Reset state if manual reset
            }


            // PID CALCULATION (How long did this take? Yes)
            double currentPos = SpinDexer.getCurrentPosition();
            double error = spinTargetPosition - currentPos;

            /*
            // --- ERROR THRESHOLD LOGIC ---
            // Self-alignment when error > 10.
            // Original logic here was flawed (subtracting full chamber), replaced by
            // dynamic power injection in the PID block below to force alignment.
            if (error > 10) {
                 // The fix is implemented in the PID calculation
            }
             */

            double dt = pidTimer.seconds(); //
            pidTimer.reset();

            // Deadzone: stop fighting if within 1 ticks
            if (Math.abs(error) < 1) {
                SpinDexer.setPower(0);
                integralSum = 0;
            } else if (dt > 0) {
                integralSum += error * dt;
                // Cap integral to prevent windup
                integralSum = Math.max(-100, Math.min(100, integralSum));

                double derivative = (error - lastError) / dt;
                double power = (error * Kp) + (integralSum * Ki) + (derivative * Kd);

                // --- PERFECT SELF-ALIGN LOGIC ---
                // If error exceeds 10, the standard PID (Kp*Error) might be too weak to move the motor.
                // We overwrite the power to a minimum "Stiction Overcoming" value to snap it into place.
                if (Math.abs(error) > 10) {
                    // If power calculated is too low to move, boost it to +/- 0.15 minimum
                    if (Math.abs(power) < 0.15) {
                        power = Math.signum(error) * 0.15;
                    }
                }

                // Limit power to 80% for "safety" we cap max power so it doesn't go crazy
                power = Math.max(-0.65, Math.min(0.65, power));
                SpinDexer.setPower(power);
                lastError = error;
            }

            // --------------------------------------- \\
            //             INTAKE LOGIC                 \\
            // ----------------------------------------- \\

            if (gamepad1.right_trigger > 0.2) {
                Intake.setPower(0.5); // INTAKE IN
            } else if (gamepad1.left_trigger > 0.2) {
                Intake.setPower(-0.75); // INTAKE OUT. Outtake???
            } else {
                Intake.setPower(0);
            }


            // --- LAUNCHER TOGGLE LOGIC ---
            if (gamepad2.y && gamepad2.a && !lastLaunch) {
                launcherOn = !launcherOn; // Flip the state (true -> false or false -> true)
            }
            lastLaunch = gamepad2.y && gamepad2.a; // Update the previous state for the next loop

            // Set motor power based on the toggle state
            if (launcherOn) {
                Top_Launch.setPower(0.5);
                Bottom_Launch.setPower(0.5);
            } else {
                Top_Launch.setPower(0);
                Top_Launch.setPower(0);
                Bottom_Launch.setPower(0);
            }



            // --- LAUNCH MOTORS ---
            //if (gamepad2.right_trigger > 0.2) {
            //Top_Launch.setPower(0.6);
            //Bottom_Launch.setPower(0.6);
            //}


            // --- KICKER LOGIC ---
            switch (kickerState) {
                case READY:
                    if (gamepad2.right_bumper) {
                        LeftKicker.setPosition(KICK_UP_POS);
                        RightKicker.setPosition(KICK_UP_POS);
                        kickerTimer.reset();
                        kickerState = KickerState.BUMPING;

                        //int chamberAtKicker = (currentIntakeChamberIndex + KICKER_OFFSET) % 3;
                        //chamberContents[chamberAtKicker] = "EMPTY";

                    } else {
                        LeftKicker.setPosition(KICK_READY_POS);
                        RightKicker.setPosition(KICK_READY_POS);
                    }
                    break;

                case BUMPING:
                    if (kickerTimer.milliseconds() > BUMP_DURATION) {
                        kickerState = KickerState.READY;
                    }
                    break;
            }

            /*
            // --- COLOR SENSOR LOGIC ---
            NormalizedRGBA colors = color_sensor_v3.getNormalizedColors();
            Color.RGBToHSV((int) (colors.red * 255), (int) (colors.green * 255), (int) (colors.blue * 255), hsv_values);

            String detected_color = "NONE";
            if (hsv_values[0] >= 200 && hsv_values[0] <= 330) detected_color = "PURPLE";
            else if (hsv_values[0] >= 65 && hsv_values[0] <= 165) detected_color = "GREEN";
            // dd0000 is a nice color :)
            // If you ever need to do color sensor just do normalized and use this website https://www.selecolor.com/en/hsv-color-picker/

            if (!detected_color.equals("NONE") && !color_detected_lately) {
                chamberContents[currentIntakeChamberIndex] = detected_color;
                detected_colors_list.add(detected_color);
                if (detected_colors_list.size() > 3) detected_colors_list.remove(0);

                // --- FAST MODE AUTO-ROTATE ---
                if (isFastMode && !isHalfway) {
                    spinTargetPosition += TICKS_PER_CHAMBER;
                    currentIntakeChamberIndex = (currentIntakeChamberIndex + 1) % 3;
                }

                color_detected_lately = true;
            } else if (detected_color.equals("NONE")) {
                color_detected_lately = false;
            }

             */

            // --- TELEMETRY ---
            telemetry.addData("MODE", isFastMode ? "FAST (Auto-Rotate)" : "NORMAL (Manual)");
            telemetry.addData("Target Pos", spinTargetPosition);
            telemetry.addData("Current Pos", currentPos);
            telemetry.addData("Error", error);
            telemetry.addData("Pos Status", isHalfway ? "HALFWAY/BLOCKED" : "ALIGNED");
            //telemetry.addData("Slot 1", chamberContents[0] + (currentIntakeChamberIndex == 0 ? " < IN" : ""));
            //telemetry.addData("Slot 2", chamberContents[1] + (currentIntakeChamberIndex == 1 ? " < IN" : ""));
            //telemetry.addData("Slot 3", chamberContents[2] + (currentIntakeChamberIndex == 2 ? " < IN" : ""));
            telemetry.update();
            telemetry.update();
            //plz say i almost done

            // ----------------- \\
            //     LIMELIGHT      \\
            // ------------------- \\
            if (gamepad1.y) {
                // *** INITIATE AUTO-AIM AND FIRE SEQUENCE ***
                performAutoAimAndFire(limelight);
            }
        }
    }

    private void performAutoAimAndFire(Limelight3A limelight) {
        double targetTolerance = 1.0; // Degrees of error considered "aligned" (0.15 is too small for most robots)
        double kP = 0.035;             // Tuning constants for P-Controller

        telemetry.addLine("Auto-Aim Initiated (Y button press)");
        telemetry.update();
        sleep(100); // Small delay to acknowledge button press

        // Loop until aligned, OpMode stops, or a different input interrupts
        while (opModeIsActive() && !gamepad2.dpad_right) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double tx = result.getTx(); // Horizontal offset from crosshair to target

                telemetry.addData("Auto-Aim Status", "Target Found, Adjusting...");
                telemetry.addData("Error (tx)", tx);

                // FIXED: Use > instead of != for the tolerance check
                if (Math.abs(tx) > targetTolerance) {
                    // Not aligned yet, calculate turn power
                    double turnPower = tx * kP;

                    // FIXED: Correct clamping logic. This ensures the robot rotates in place.
                    // Max turn power is 0.4, Min is 0.1 to overcome friction.
                    if (Math.abs(turnPower) > 0.4) turnPower = Math.signum(turnPower) * 0.4;
                    if (Math.abs(turnPower) < 0.1) turnPower = Math.signum(turnPower) * 0.1;

                    // Apply to Mecanum wheels for a pure rotation
                    Fleft.setPower(turnPower);
                    Bleft.setPower(turnPower);
                    Fright.setPower(-turnPower);
                    Bright.setPower(-turnPower);

                } else {
                    // Aligned! Stop motors
                    stopDriveMotors();
                    telemetry.addData("Auto-Aim Status", "LOCKED ON");
                    telemetry.update();

                    // Optional: If you want to automatically fire, you would add kicker code here

                    telemetry.addData("Auto-Aim Status", "SEQUENCE COMPLETE - Returning to Manual Control");
                    telemetry.update();
                    break; // Exit the while loop
                }
            } else {
                // No target found, stop motors and exit
                stopDriveMotors();
                telemetry.addData("Auto-Aim Status", "Target NOT Found! Stopping sequence.");
                telemetry.update();
                break; // Exit the while loop
            }

            telemetry.update();
        }

        // Add a short sleep after the sequence finishes
        sleep(300);
    }

    private void stopDriveMotors() {
        Fleft.setPower(0);
        Bleft.setPower(0);
        Fright.setPower(0);
        Bright.setPower(0);
    }
}
