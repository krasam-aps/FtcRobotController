//      |---|  /---/  |---====--\  |----------|   /---------|  |---|   |---|         |---====--\
//      |   | /   /   |   |  |  |  |---    ---|  |          /  |   |   |   |         |   |  |  |
//      |   |/   /    |   '--'  /      |  |      |    -----    |   |---|   |         |   '--'  /
//      |       |     |       \        |  |       \        \   |           |         |       \
//      |   |\   \    |   |\   \       |  |        -----    |  |   |---|   |         |   |\   \
//      |   | \   \   |   | \   \  |---    ---|  /          |  |   |   |   |  |---|  |   | \   \
//      |---|  \---\  |---|  \---\ |----------|  |---------/   |---|   |---|  |---|  |---|  \---\

//      \---\ /---/      /\     |----------| |----------| |----------| |---|
//       \   |   /      /  \    |---    ---| |----     -| |---    ---| |   |
//        \     /      /    \       |  |         /    /       |  |     |   |
//         \   /      /  /\  \      |  |        /    /        |  |     |   |       is gone for now :(
//         |   |     /        \     |  |       /    /         |  |     |   |
//         |   |    /   /--\   \    |  |     |-     ----| |---    ---| |   -------|
//         |---|   /---/    \---\   |--|     |----------| |----------| |----------|
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class yatzilsAUTO extends LinearOpMode {
    DcMotor Fleft = null;
    DcMotor Fright = null;
    DcMotor Bleft = null;
    DcMotor Bright = null;
    DcMotor Intake;
    DcMotor Launch1;
    CRServo myCRservo1;
    CRServo myCRservo2;
    Servo myGateservo1;
    double power = 1.0;

    /*
    LIST OF ALL CASES:
    camelCase - The best one!!!
    snakeCase - Not okay, you're crazy if u use this
    Dot.Case - Truce made between Krish & CJ, it is mid.
    myYatzilCase - made by Yatzil, and basically SUCKS
    */


    public void runOpMode(){
        Fleft = hardwareMap.get(DcMotor.class,"Fleft");
        Fright = hardwareMap.get(DcMotor.class,"Fright");
        Bleft = hardwareMap.get(DcMotor.class,"Bleft");
        Bright = hardwareMap.get(DcMotor.class,"Bright");
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Launch1 = hardwareMap.get(DcMotor.class,"Launch1");
        myCRservo1 = hardwareMap.get(CRServo.class, "myCRservo1");
        myCRservo2 = hardwareMap.get(CRServo.class, "myCRservo2");
        myGateservo1 = hardwareMap.get(Servo.class, "myGateservo1");

        myGateservo1.scaleRange(-1.0, 1.0);

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Intake.setDirection(DcMotorSimple.Direction.REVERSE);

        //ks = static gain = ??????????????????? = location??????
        //kv = velocity = speed
        //ka = acceleration = time to get to speed
        //ur welcome, future krish

        /*
        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.05, 0.11, 0.31)
        Launch1.setPower (feedforward.calclate(5, ))
         */

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Launch1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Launch1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();
        if (opModeIsActive()) {
            //the VERY least
            resetEncoder();
            forward(-0.5, -3250);// origin ticks 3900
            sleeep(1000);
            myGateservo1.setPosition(0.4);
            launchWintake(-0.41, -20000, 3000); // origin power 40
            sleeep(1000);
            launchWintake(-0.60, -20000, 3000); //origin power 45
            sleeep(1000);
            launchWintake(-0.37, -20000, 2000); //origin power 50
            sleeep(1000);
            myGateservo1.setPosition(1);
            turn(0.5, 375);
            sleeep(500);
            forward(0.5, -500);
            sleeep(500);
            turn(0.5, -750);
            sleeep(1000);

            /*turn(0.5, 1100);
            sleep(1400);
            resetEncoder();
            strafe(0.47, -400); //strafe left
            sleep(1000);
            resetEncoder();
            forward(0.5, -1200);
            sleep(1500);
            resetEncoder();
            Intake(0.5, 1000);
            sleep(1000);
            resetEncoder();
            //backwardWintake(0.5, 1200);
            //sleep(1500);
            //resetEncoder();
            forward(0.5, 1200);
            sleep(1500);
            resetEncoder();
            strafe(0.47, 400); //strafe left
            sleep(1000);
            resetEncoder();
            turn(0.5, -1100);
            sleep(1400);
            resetEncoder();
            turn(0.5, 1100);
            sleep(1400);
            resetEncoder();
            strafe(0.47, -900); //strafe left
            sleep(1000);
            resetEncoder();
            forward(0.5, -1050);
            sleep(1200);
            resetEncoder();
            Intake(0.5, 1000);
            sleep(1000);
            resetEncoder();
            //backwardWintake(0.5, 1050);
            sleep(1000);
            resetEncoder();
            forward(0.5, 1050);
            sleep(1200);
            resetEncoder();
            strafe(0.47, 900); //strafe left
            sleep(1000);
            resetEncoder();
            turn(0.5, -1100);
            sleep(1400);
            resetEncoder();
            strafe(1, 200);
            sleep(1000);
            resetEncoder();
            /*turn(0.5, 1200);
            sleep(1400);
            resetEncoder();
            strafe(0.5,-1150);
            sleep(1200);
            resetEncoder();
            backwardWintake(0.9, 1100);
            sleep(1400);
            resetEncoder();
            forward(0.5, 1100);
            sleep(1400);
            resetEncoder();
            strafe(0.5, 1150);
            sleep(1200);
            resetEncoder();
            turn(0.5, -1200);
            sleep(1400);
            resetEncoder();





            //the moreish more
            /*
            turn(0.5, 1800);
            sleep(900);
            resetEncoder();
            backwardWintake(-0.5, -1900);
            sleep(1300);
            resetEncoder();
            turn(0.5, -1800);
            sleep(1000);
            resetEncoder();
            strafe(0.5, -1000);
            turn(0.5, 1800);
            sleep(1000);
            resetEncoder();
            launchWintake(0.9, 2000, 3000);
            resetEncoder();*/

            /*telemetry.addData("Fleft:",frontLeft.getCurrentPosition());
            telemetry.addData("Fright:",frontRight.getCurrentPosition());
            telemetry.addData("Bleft:",backLeft.getCurrentPosition());
            telemetry.addData("Bright:",backRight.getCurrentPosition());
            telemetry.addData("Launch1:",topLauncherMotor.getCurrentPosition());
            telemetry.addData("Intake:",intake.getCurrentPosition());
            telemetry.update();
            sleep(5000);*/
        }
    }

    //              ___
    //             |o-o| hi im bob
    //              |-|
    // this is bob, the protector of the methods
    // aggro him and you will not be spared

    //-strafe is strafeLeft, strafe is strafeRight
    public void sleeep(int milliseconds){
        //Sleeep(); is sleep(); but better!
        //It was created fore the sole purpouse of using less lines of code & making it better.
        //This is basically the sleep(); command, but with resetEncoder(); baked into it
        //So, it's sleep but better!
        //Hopefully, it will convince snakeCase (CJ) to go to bed for once.
        //It's been a year, go take a nap!!!!!
        sleep(milliseconds);
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

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Fright.setPower(power);
        Bleft.setPower(power);
        Bright.setPower(power);
        Intake.setPower(power);
        myCRservo1.setPower(power);
        myCRservo2.setPower(power);
    }
    /*public void backward(double power, int ticks){
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
    }*/
    //strafeRight can go northeast (strafeRight) and southwest (-strafeRight)
    public void diagonalRight(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fleft.setPower(power);
        Bright.setPower(power);
    }
    //strafeLeft can go northwest (strafeLeft) and southeast (-strafeLeft)
    public void diagonalLeft(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);

        Fleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Bright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Fright.setPower(power);
        Bleft.setPower(power);
    }
    public void launchWintake(double power, int ticks, int time){
        Launch1.setTargetPosition(-ticks);
        Intake.setTargetPosition(ticks);

        Launch1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Launch1.setPower(power);
        sleep(1500);
        Intake.setPower(power);
        myCRservo1.setPower(-1);
        myCRservo2.setPower(-1);
        sleep(1200);


    }
    public void Intake(double power, int ticks){
        Intake.setTargetPosition(ticks);

        Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Intake.setPower(power);
        myCRservo1.setPower(power);
        myCRservo2.setPower(power);
    }
    //rotation is turning right, -rotation is turning left
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
        Launch1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
}




    /*public void turnRight(double power, int ticks){
        Fleft.setTargetPosition(ticks);
        B.setTargetPosition(ticks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        backLeft.setPower(power);
    }
    public void turnLeft(double power, int ticks){
        frontRight.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setPower(power);
        backRight.setPower(power);
    }
}*/