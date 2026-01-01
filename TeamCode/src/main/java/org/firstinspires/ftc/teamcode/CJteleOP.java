package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "CJteleOP", group = "Linear OpMode")
public class CJteleOP extends LinearOpMode {

    // Hardware
    DcMotor Fleft, Bleft, Fright, Bright, SpinDexer, Top_Launch, Bottom_Launch, Intake;
    //intake go brr
    Servo LeftKicker, RightKicker;
    private NormalizedColorSensor color_sensor_v3;
    Limelight3A limelight;

    // Movement Variables
    double speedMultiplier = 0.75;

    // Spindexer Constants
    // Using 251 as an int to prevent "jitter" from decimal targets
    int TICKS_PER_CHAMBER = 251;
    int spinTargetPosition = 0;

    // PID Coefficients
    double Kp = 0.005;
    double Ki = 0.0;
    double Kd = 0.0001;
    double integralSum = 0;
    double lastError = 0;
    ElapsedTime pidTimer = new ElapsedTime();

    // State Tracking
    String[] chamberContents = {"EMPTY", "EMPTY", "EMPTY"};
    int currentIntakeChamberIndex = 0;
    int KICKER_OFFSET = 1;

    enum KickerState {READY, BUMPING}

    KickerState kickerState = KickerState.READY;
    ElapsedTime kickerTimer = new ElapsedTime();

    // Servo Positions
    double KICK_READY_POS = 0.25;
    double KICK_UP_POS = 0.5;
    double BUMP_DURATION = 150;

    // Color Logic
    float[] hsv_values = new float[3];

    private List<String> detected_colors_list = new ArrayList<>();
    boolean color_detected_lately = false;

    // Toggle states
    boolean lastA = false;
    boolean lastB = false;
    boolean lastToggle = false; // For mode switching (Fast/Normal)
    boolean isFastMode = true;  // Fast is default

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
        color_sensor_v3 = hardwareMap.get(NormalizedColorSensor.class, "color_sensor_v3");
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
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rot = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
            Fleft.setPower(((y + x + rot) / denominator) * speedMultiplier);
            Fright.setPower(((y - x - rot) / denominator) * speedMultiplier);
            Bleft.setPower(((y - x + rot) / denominator) * speedMultiplier);
            Bright.setPower(((y + x - rot) / denominator) * speedMultiplier);

            if (gamepad1.left_bumper) speedMultiplier = 0.5;
            else if (gamepad1.right_bumper) speedMultiplier = 1.0;
            else speedMultiplier = 0.75;

            // --- MODE TOGGLE LOGIC ---
            // D-Pad Up toggles between Fast and Normal modes
            if (gamepad1.dpad_up && !lastToggle) {
                isFastMode = !isFastMode;
            }
            lastToggle = gamepad1.dpad_up;

            // --- SPINDEXER TARGET LOGIC ---
            if (gamepad1.a && !lastA) {
                spinTargetPosition += TICKS_PER_CHAMBER;
                currentIntakeChamberIndex = (currentIntakeChamberIndex + 1) % 3;
            } else {
                SpinDexer.setPower(0);
            }
            lastA = gamepad1.a;

            if (gamepad1.b && !lastB) {
                spinTargetPosition -= TICKS_PER_CHAMBER;
                currentIntakeChamberIndex = (currentIntakeChamberIndex - 1);
                if (currentIntakeChamberIndex < 0) currentIntakeChamberIndex = 2;
            } else {
                SpinDexer.setPower(0);
            }
            lastB = gamepad1.b;

            if (gamepad1.dpad_left) {
                SpinDexer.setPower(0.25);
                spinTargetPosition = 0;
            }

            // PID CALCULATION (How long did this take? Yes)
            double currentPos = SpinDexer.getCurrentPosition();
            double error = spinTargetPosition - currentPos;


            /*
            // --- ERROR THRESHOLD LOGIC ---
            // If the error is MORE than 10, move backward one chamber (until error >= 10)
            if (error > 10) {
                spinTargetPosition -= TICKS_PER_CHAMBER;
                currentIntakeChamberIndex = (currentIntakeChamberIndex - 1);
                if (currentIntakeChamberIndex < 0) currentIntakeChamberIndex = 2;

                // Recalculate error immediately after adjusting target
                error = spinTargetPosition - currentPos;
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

                // Limit power to 45% for "safety" we cap max power so it doesn't go crazy
                power = Math.max(-0.80, Math.min(0.80, power)); // This was never meant to be 1... or to be so high... Dear God...
                SpinDexer.setPower(power);
                lastError = error;
            }

            // --------------------------------------- \\
            //             INTAKE LOGIC                 \\
            // ----------------------------------------- \\

            if (gamepad1.right_trigger > 0.2) {
                Intake.setPower(0.55); // INTAKE IN
            } else if (gamepad1.left_trigger > 0.2) {
                Intake.setPower(-0.75); // INTAKE OUT. Outtake???
            } else {
                Intake.setPower(0);
            }

            // --- LAUNCH MOTORS ---
            if (gamepad1.x) {
                Top_Launch.setPower(0.8);
                Bottom_Launch.setPower(0.8);
            } else {
                Top_Launch.setPower(0);
                Bottom_Launch.setPower(0);
            }

            // --- KICKER LOGIC ---
            switch (kickerState) {
                case READY:
                    if (gamepad2.right_bumper) {
                        LeftKicker.setPosition(KICK_UP_POS);
                        RightKicker.setPosition(KICK_UP_POS);
                        kickerTimer.reset();
                        kickerState = KickerState.BUMPING;

                        int chamberAtKicker = (currentIntakeChamberIndex + KICKER_OFFSET) % 3;
                        chamberContents[chamberAtKicker] = "EMPTY";

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
                if (isFastMode) {
                    spinTargetPosition += TICKS_PER_CHAMBER;
                    currentIntakeChamberIndex = (currentIntakeChamberIndex + 1) % 3;
                }

                color_detected_lately = true;
            } else if (detected_color.equals("NONE")) {
                color_detected_lately = false;
            }

            // --- TELEMETRY ---
            telemetry.addData("MODE", isFastMode ? "FAST (Auto-Rotate)" : "NORMAL (Manual)");
            telemetry.addData("Target Pos", spinTargetPosition);
            telemetry.addData("Current Pos", currentPos);
            telemetry.addData("Error", error);
            telemetry.addData("Slot 1", chamberContents[0] + (currentIntakeChamberIndex == 0 ? " < IN" : ""));
            telemetry.addData("Slot 2", chamberContents[1] + (currentIntakeChamberIndex == 1 ? " < IN" : ""));
            telemetry.addData("Slot 3", chamberContents[2] + (currentIntakeChamberIndex == 2 ? " < IN" : ""));
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
        double targetTolerance = 0.15; // Degrees of error considered "aligned"
        double kP = 0.01;             // Tuning constants for P-Controller (slightly lower kP for precision)

        telemetry.addLine("Auto-Aim Initiated (Y button press)");
        telemetry.update();
        sleep(500); // Small delay to acknowledge button press

        // Loop until aligned, OpMode stops, or a different input interrupts (like another gamepad press)
        while (opModeIsActive() && !gamepad1.y) { // Use B as an emergency stop for auto-aim
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double tx = result.getTx(); // Horizontal offset from crosshair to target

                telemetry.addData("Auto-Aim Status", "Target Found, Adjusting...");
                telemetry.addData("Error (tx)", tx);
                telemetry.addData("Tolerance", targetTolerance);

                if (Math.abs(tx) != targetTolerance) {
                    // Not aligned yet, keep turning
                    double turnPower = tx * kP;

                    // Cap max power for smooth movement
                    if (turnPower > 0.4) turnPower = -0.4;
                    if (turnPower < -0.4) turnPower = 0.4;

                    Fleft.setPower(turnPower - .2);
                    Bleft.setPower(turnPower - .2);
                    Fright.setPower(-turnPower);
                    Bright.setPower(-turnPower);

                } else {
                    // Aligned! Stop motors and prepare to fire
                    stopDriveMotors();
                    telemetry.addData("Auto-Aim Status", "LOCKED ON - FIRING IN 1 SECOND");
                    telemetry.update();

                    // Firing sequence:
                    // 1. Start the shooter motor
                    // Launch1.setPower(0.55);
                    // sleep(1000); // Let motor spin up for 1 second

                    // Reset everything and exit the auto-aim sequence loop
                    Top_Launch.setPower(0);
                    Bottom_Launch.setPower(0);
                    stopDriveMotors();
                    telemetry.addData("Auto-Aim Status", "SEQUENCE COMPLETE - Returning to Manual Control");
                    telemetry.update();
                    break; // Exit the while loop to return to the main loop manual control
                }
            } else {
                // No target found, stop motors and exit
                stopDriveMotors();
                Top_Launch.setPower(0);
                Bottom_Launch.setPower(0);
                telemetry.addData("Auto-Aim Status", "Target NOT Found! Stopping sequence.");
                telemetry.update();
                break; // Exit the while loop
            }

            telemetry.update();
        }

        // Add a short sleep after the sequence finishes to prevent immediate re-triggering if Y is still held down briefly
        sleep(300);
    }

    private void stopDriveMotors() {
        Fleft.setPower(0);
        Bleft.setPower(0);
        Fright.setPower(0);
        Bright.setPower(0);
    }
}