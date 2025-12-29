//      |---|  /---/  |---====--\  |----------|   /---------|  |---|   |---|         |---====--\
//      |   | /   /   |   |  |  |  |---    ---|  |          /  |   |   |   |         |   |  |  |
//      |   |/   /    |   '--'  /      |  |      |    -----    |   |---|   |         |   '--'  /
//      |       |     |       \        |  |       \        \   |           |         |       \
//      |   |\   \    |   |\   \       |  |        -----    |  |   |---|   |         |   |\   \
//      |   | \   \   |   | \   \  |---    ---|  /          |  |   |   |   |  |---|  |   | \   \
//      |---|  \---\  |---|  \---\ |----------|  |---------/   |---|   |---|  |---|  |---|  \---\
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp()
public class TeleopTest_ extends LinearOpMode {
    DcMotor Fleft;
    DcMotor Bleft;
    DcMotor Fright;
    DcMotor Bright;
    DcMotor Intake;
    DcMotor Launch1;
    CRServo myCRservo1;
    CRServo myCRservo2;
    Servo myGateservo1;
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

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
            double FLpower = (y + x + rot) / denominator;
            double FRpower = (y - x - rot) / denominator;
            double BLpower = (y - x + rot) / denominator;
            double BRpower = (y + x - rot) / denominator;

            if (gamepad1.left_bumper) {
                speedMultiplier = 0.5;
            } else if (gamepad1.right_bumper) {
                speedMultiplier = 1;
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
            if (gamepad1.right_stick_x > 0) {
                //myBrakeservo1.setPosition(0)
                //myBrakeservo2.setPosition(0)
            } else if (gamepad1.right_stick_y > 0) {
                //myBrakeservo1.setPosition(0)
                //myBrakeservo2.setPosition(0)
            } else if (gamepad1.right_stick_x < 0) {
                //myBrakeservo1.setPosition(0)
                //myBrakeservo2.setPosition(0)
            } else if (gamepad1.right_stick_y < 0) {
                //myBrakeservo1.setPosition(0)
                //myBrakeservo2.setPosition(0)
            } else {
                //myBrakeservo1.setPosition(1)
                //myBrakeservo2.setPosition(1)
            }
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
                myGateservo1.setPosition(0.4);
                Launch1.setPower(.67);
                //SIX SEVEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //MASON 67 HAS BLESSED US
                //BOW DOWN TO SIX SEEEEEVEEEEENNNNNNNN!!!!!!!!!
                myGateservo1.setPosition(0.4);
                // Do not touch Gate Servo
                // All Gate Servo code was finalized on 10/25/25 at 11:13am
            } else {
                Launch1.setPower(0);
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

        }
    }

    /*
    Snake_Case < Camelcase
    Cope Harder!
     */
}