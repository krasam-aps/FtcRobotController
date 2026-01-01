//      |---|  /---/  |---====--\  |----------|   /---------|  |---|   |---|         |---====--\
//      |   | /   /   |   |  |  |  |---    ---|  |          /  |   |   |   |         |   |  |  |
//      |   |/   /    |   '--'  /      |  |      |    -----    |   |---|   |         |   '--'  /
//      |       |     |       \        |  |       \        \   |           |         |       \
//      |   |\   \    |   |\   \       |  |        -----    |  |   |---|   |         |   |\   \
//      |   | \   \   |   | \   \  |---    ---|  /          |  |   |   |   |  |---|  |   | \   \
//      |---|  \---\  |---|  \---\ |----------|  |---------/   |---|   |---|  |---|  |---|  \---\

package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.graphics.Color;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import java.util.ArrayList;
import java.util.List;

@Autonomous
public class autoenomous extends LinearOpMode {
    DcMotor Fleft = null;
    DcMotor Fright = null;
    DcMotor Bleft = null;
    DcMotor Bright = null;
    DcMotor Intake;
    DcMotor Launch1;
    DcMotor Launch2;
    DcMotor Spindexer;
    Servo RightKicker, LeftKicker;

    double power = 1.0;

    int spinTargetPosition = 0;
    int TICKS_PER_CHAMBER = 251;

    // PID Coefficients
    double Kp = 0.005;
    double Ki = 0.0;
    double Kd = 0.0001;
    double integralSum = 0;
    double lastError = 0;
    ElapsedTime pidTimer = new ElapsedTime();

    @Override
    public void runOpMode(){
        Fleft = hardwareMap.get(DcMotor.class,"Fleft");
        Fright = hardwareMap.get(DcMotor.class,"Fright");
        Bleft = hardwareMap.get(DcMotor.class,"Bleft");
        Bright = hardwareMap.get(DcMotor.class,"Bright");
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Launch1 = hardwareMap.get(DcMotor.class,"Launch1");
        Launch2 = hardwareMap.get(DcMotor.class,"Launch2");
        Spindexer = hardwareMap.get(DcMotor.class,"Spindexer");
        RightKicker = hardwareMap.get(Servo.class,"RightKicker");
        LeftKicker = hardwareMap.get(Servo.class,"LeftKicker");

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Launch2.setDirection(DcMotorSimple.Direction.FORWARD);
        Launch1.setDirection(DcMotorSimple.Direction.REVERSE);
        RightKicker.setDirection(Servo.Direction.REVERSE);
        LeftKicker.setDirection(Servo.Direction.FORWARD);

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Launch1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Launch2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Spindexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Spindexer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        pidTimer.reset();

        if (opModeIsActive()) {
            resetEncoder();
            SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 0.1125, 0.27);
            double powerRating = feedforward.calculate(3.4);
            forward(0.5, 150);
            sleep(1000);
            resetEncoder();
            turn(0.5, -140);
            sleeep(300);
            Launch1.setPower(powerRating);
            Launch2.setPower(powerRating); //top
            telemetry.addData("powerRating = ", powerRating);
            telemetry.update();
            sleeep(1500);
            Kicker(1, 300);
            sleeep(500);
            Spindexer(0.8, 251);
            sleeep(300);
            Kicker(1, 300);
            sleeep(500);
            Spindexer(0.8, 251);
            sleeep(300);
            Kicker(1, 300);
            sleeep(500);
            Spindexer(0.8, 376);
            sleeep(300);
        }
    }
    
    //         _
    //        / \
    //        | |      ____________
    //        | |      |__      __|
    //        | |      |x \    / x|__/___\    "bobuu es dead" - Bob
    //      _|___|_    |_  ( _____(_(_(_(|          I
    //      <---------------------------------------I
    //     <         SWORD OF THE GREAT BOB         I===============
    //      <---------------------------------------I
    //        |_|      |  _  |      |               I
    //                 |/   \|
    //                 |     |
    //             /___|     |___\
    // this is Bobuu, the protector of the fake, snake_case methods
    // and now he is ded, k*lled by BOB
    // CAMELCASE HAS RECLAIMED THIS AUTOENOMOUS
    // BOW DOWN TO BOB, THE ULTIMATE DESTROYER

    public void Kicker(double power, long milliseconds){
        LeftKicker.setPosition(0.5);
        RightKicker.setPosition(0.5);
        sleep(milliseconds);
        LeftKicker.setPosition(0.25);
        RightKicker.setPosition(0.25);
    }

    public void Spindexer(double power, int ticks){
        spinTargetPosition += ticks;
        // Hold until Spindexer reaches target via PID
        ElapsedTime timeout = new ElapsedTime();
        while(opModeIsActive() && Math.abs(spinTargetPosition - Spindexer.getCurrentPosition()) > 2 && timeout.seconds() < 2){
            updateSpindexerPID();
        }
    }

    // PID CALCULATION METHOD
    public void updateSpindexerPID() {
        double currentPos = Spindexer.getCurrentPosition();
        double error = spinTargetPosition - currentPos;
        double dt = pidTimer.seconds();
        pidTimer.reset();

        if (Math.abs(error) < 1) {
            Spindexer.setPower(0);
            integralSum = 0;
        } else if (dt > 0) {
            integralSum += error * dt;
            integralSum = Math.max(-100, Math.min(100, integralSum));

            double derivative = (error - lastError) / dt;
            double pidPower = (error * Kp) + (integralSum * Ki) + (derivative * Kd);

            if (Math.abs(error) > 10) {
                if (Math.abs(pidPower) < 0.15) {
                    pidPower = Math.signum(error) * 0.15;
                }
            }

            pidPower = Math.max(-0.95, Math.min(0.95, pidPower));
            Spindexer.setPower(pidPower);
            lastError = error;
        }
    }

    public void sleeep(int milliseconds){
        ElapsedTime waitTimer = new ElapsedTime();
        while(opModeIsActive() && waitTimer.milliseconds() < milliseconds) {
            updateSpindexerPID(); // Keep PID active during sleeps
        }
        resetEncoder();
    }

    public void strafe(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(-ticks);
        Bleft.setTargetPosition(-ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Fright.setPower(power);
        Bleft.setPower(power);
        Bright.setPower(power);
    }

    public void forward(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Fright.setPower(power);
        Bleft.setPower(power);
        Bright.setPower(power);
    }

    public void backwardWintake(double power, int ticks){
        Fleft.setTargetPosition(-ticks);
        Fright.setTargetPosition(-ticks);
        Bleft.setTargetPosition(-ticks);
        Bright.setTargetPosition(-ticks);
        Intake.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Fright.setPower(power);
        Bleft.setPower(power);
        Bright.setPower(power);
        Intake.setPower(power);
    }

    public void backward(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(-power);
        Fright.setPower(-power);
        Bleft.setPower(-power);
        Bright.setPower(-power);
    }

    public void diagonalRight(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);
        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fleft.setPower(power);
        Bright.setPower(power);
    }

    public void diagonalLeft(double power, int ticks){
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setPower(power);
        Bleft.setPower(power);
    }

    public void Flywheel(double power, int time){
        Launch1.setPower(1);
        sleep(time);
        Launch1.setPower(0);
    }

    public void Intake(double power, int ticks){
        Intake.setTargetPosition(ticks);
        Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Intake.setPower(power);
    }

    public void turn(double power, int ticks){
        Fleft.setTargetPosition(-ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(-ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Bleft.setPower(power);
        Fright.setPower(power);
        Bright.setPower(power);
    }

    public void resetEncoder() {
        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}