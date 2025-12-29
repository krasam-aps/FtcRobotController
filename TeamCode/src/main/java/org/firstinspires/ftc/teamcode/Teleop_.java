//      |---|  /---/  |---====--\  |----------|   /---------|  |---|   |---|         |---====--\
//      |   | /   /   |   |  |  |  |---    ---|  |          /  |   |   |   |         |   |  |  |
//      |   |/   /    |   '--'  /      |  |      |    -----    |   |---|   |         |   '--'  /
//      |       |     |       \        |  |       \        \   |           |         |       \
//      |   |\   \    |   |\   \       |  |        -----    |  |   |---|   |         |   |\   \
//      |   | \   \   |   | \   \  |---    ---|  /          |  |   |   |   |  |---|  |   | \   \
//      |---|  \---\  |---|  \---\ |----------|  |---------/   |---|   |---|  |---|  |---|  \---\
package org.firstinspires.ftc.teamcode;

//import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp()
public class Teleop_ extends LinearOpMode {
    DcMotor Fleft;
    DcMotor Bleft;
    DcMotor Fright;
    DcMotor Bright;
    DcMotor Intake;
    DcMotor Launch1;
    CRServo myCRservo1;
    CRServo myCRservo2;
    Servo myGateservo1;

    //Limelight3A limelight;

    double y = 0.0;
    double rot = 0.0;
    double x = 0.0;
    double TLMpower = 0.0;
    double BLMpower = 0.0;
    double CRISP = 0.0;
    double speedMultiplier = 0.75;


    @Override
    public void runOpMode() {
        // CRISP stands for CONTINUOUS ROTATION INTAKE SERVO POWER, would be better as KRISH,
        // but we don't have a K and an H.
        Fleft = hardwareMap.get(DcMotor.class, "Fleft");
        Fright = hardwareMap.get(DcMotor.class, "Fright");
        Bleft = hardwareMap.get(DcMotor.class, "Bleft");
        Bright = hardwareMap.get(DcMotor.class, "Bright");
        Launch1 = hardwareMap.get(DcMotor.class, "Launch1");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        myCRservo1 = hardwareMap.get(CRServo.class, "myCRservo1");
        myCRservo2 = hardwareMap.get(CRServo.class, "myCRservo2");
        myGateservo1 = hardwareMap.get(Servo.class, "myGateservo1");
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.start();

        // Limelight Pipeline
        limelight.pipelineSwitch(0);

        //int i = Integer.parseInt(null);
        LimeLightImageTools llIt = new LimeLightImageTools(limelight);
        llIt.setDriverStationStreamSource();
        llIt.forwardAll();
        //FtcDashboard.getInstance().startCameraStream(llIt.getStreamSource(), 59.999);

        myGateservo1.scaleRange(-1.0, 1.0);

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        myCRservo1.setDirection(DcMotorSimple.Direction.REVERSE);
        myCRservo2.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

            while (opModeIsActive()) {
            y = -gamepad1.right_stick_y;
            rot = gamepad1.left_stick_x;
            x = gamepad1.right_stick_x;
            Launch1.setPower(.58);

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
            double FLpower = (y + x + rot) / denominator;
            double FRpower = (y - x - rot) / denominator;
            double BLpower = (y - x + rot) / denominator;
            double BRpower = (y + x - rot) / denominator;

            if (gamepad1.left_bumper) {
                speedMultiplier = 0.5;
                telemetry.addLine("Going Slow");
            } else if (gamepad1.right_bumper) {
                speedMultiplier = 1;
                telemetry.addLine("Going Fast!");
            } else {
                speedMultiplier = 0.75;
            }

            Fleft.setPower(FLpower * speedMultiplier);
            Fright.setPower(FRpower * speedMultiplier);
            Bleft.setPower(BLpower * speedMultiplier);
            Bright.setPower(BRpower * speedMultiplier);

            if (gamepad1.right_trigger > 0.2) {
                Intake.setPower(1.0);
                myCRservo1.setPower(1.0);
                myCRservo2.setPower(1.0);
            } else {
                Intake.setPower(0);
                myCRservo1.setPower(0);
                myCRservo2.setPower(0);
            }
            if (gamepad1.left_trigger > 0.2) {
                Intake.setPower(-1.0);
                myCRservo1.setPower(-1.0);
                myCRservo2.setPower(-1.0);
            } else {
                Intake.setPower(0);
                myCRservo1.setPower(0); // comment
                myCRservo2.setPower(0); // comment^2
            } // Pi * comment^3

            if (gamepad2.left_trigger > 0.2) {
                myCRservo1.setPower(1.0);
                myCRservo2.setPower(1.0);
                Intake.setPower(1.0);
            } else {
                myCRservo1.setPower(0);
                myCRservo2.setPower(0);
                Intake.setPower(0);
            }
            if (gamepad2.right_trigger > 0.2) {
                myGateservo1.setPosition(0.33);
                //Launch1.setPower(.58);
                //myGateservo1.setPosition(0.35);
                // Do not touch Gate Servo
                // All Gate Servo code was finalized on 10/25/25 at 11:13am
            } else {

                myGateservo1.setPosition(1);
                // Do not touch Gate Servo
                // All Gate Servo code was finalized on 10/25/25 at 11:13am
            }

            //Top CRservo go backwards... would be helpful... test this l8r krish
            if (gamepad2.dpad_down) {
                myCRservo2.setPower(-1.0);
            } else {
                myCRservo2.setPower(0);
            }

            if (gamepad1.y) {
                // *** INITIATE AUTO-AIM AND FIRE SEQUENCE ***
                performAutoAimAndFire(limelight);
            }

        }
    }

    /*
    Snake_Case < Camelcase
    Cope Harder!
     */

private void performAutoAimAndFire(Limelight3A limelight) {
    double targetTolerance = 0.25; // Degrees of error considered "aligned"
    double kP = 0.05;             // Tuning constants for P-Controller (slightly lower kP for precision)

    telemetry.addLine("Auto-Aim Initiated (Y button press)");
    telemetry.update();
    sleep(500); // Small delay to acknowledge button press

    // Loop until aligned, OpMode stops, or a different input interrupts (like another gamepad press)
    while (opModeIsActive() && !gamepad1.b) { // Use B as an emergency stop for auto-aim
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
                Launch1.setPower(0.55);
                sleep(1000); // Let motor spin up for 1 second

                // 2. Open the gate
                myGateservo1.setPosition(0.44);
                sleep(1500); // Keep gate open long enough for the ring to leave

                // 3. Reset everything and exit the auto-aim sequence loop
                Launch1.setPower(0);
                myGateservo1.setPosition(1);
                stopDriveMotors();
                telemetry.addData("Auto-Aim Status", "SEQUENCE COMPLETE - Returning to Manual Control");
                telemetry.update();
                break; // Exit the while loop to return to the main loop manual control
            }
        } else {
            // No target found, stop motors and exit
            stopDriveMotors();
            Launch1.setPower(0);
            myGateservo1.setPosition(1);
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
