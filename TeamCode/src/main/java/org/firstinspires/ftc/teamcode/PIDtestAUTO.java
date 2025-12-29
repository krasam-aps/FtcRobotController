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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class PIDtestAUTO extends LinearOpMode {
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

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Launch1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Launch1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //ks = static gain = location??????
        //kv = velocity = speed
        //ka = acceleration = time to get to speed
        //ur welcome, future krish

        /*
        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.05, 0.11, 0.31)
        Launch1.setPower (feedforward.calculate(5, ))
         */

        /*
        PID testing log
        11/28/25, 3:23 PM - Problem... Got JOSH's code for PID, just started, and the
           FLYWHEEL WON'T TURN AT ALL needs some bugfixing will read the
           FTC docs now...
        11/28/25, 4:03 PM - THE PID FOR THE FLYWHEEL WILL STILL NOT WORK FOR GODS SAKE
           I CAN'T GET THIS THINGGGG To WORKKKKKKK PLEASE GRADLE DAEMON
           BLESS ME WITH A WORKING PID CONTROLLER FOR THE FLYWHEEL PLEASEEEEEE
        11/28/25 4:14 PM - i've basically given up this pid won't work
        11/28/25 4:21 PM - EEAUUUUGHHHH this PID STUFF is so FREAKING CONFUSING and it
           won't even REASON with me and the flywheel has made ZERO MOTION AT ALL and i
           just want to go home... eeauughh
        11/28/25 4:32 PM - Yeah, i'm going crazy. i'm ALREADY CRAZY look at me lazy-eyed
           looking at this StU-PiD (get it? stu-PID? P.I.D controller? i'm definitely crazy)
           cOnTrOlLeR qoemfofjkeemovijnjovkoenvoemcivrgnidnk
        11/28/25 4:55 PM - Today, i got NUTHING done. I can't fix the error, and whatever
           i do doesn't change the output... Launch1 power set to 0. I can't fix it, so
           I will inquire JOSH tommorow, on the 29th.
        11/28/25 5:21 PM - yeah, this is a lost cause without JOSH here. I can't undertand
           PID, and however much I try, it just gets more complicated...
        12/4/2025
         */
        waitForStart();


        if (opModeIsActive()) {
            forward(-0.5, -750);// origin ticks 3900
            myGateservo1.setPosition(1);
            //sleeep(250);
            /*
            // Create a new SimpleMotorFeedforward with gains kS, kV, and kA
                  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kS, kV, kA);

            // Calculates the feedforward for a velocity of 10 units/second
            // and an acceleration of 20 units/second^2
            // Units are determined by the units of the gains passed
            // in at construction.
                   feedforward.calculate(10, 20);
             */
            telemetry.addData("Launch1",Launch1.getCurrentPosition());
            telemetry.update();
            //sleeep(2500)
            // 0, .146, 0.277

            SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 0.1125, 0.27); //ka=.27
            // 4
            double powerRating = feedforward.calculate(3.4);
            telemetry.addData("Launch1 powerRating: ", powerRating);
            telemetry.update();
            Launch1.setPower(powerRating);
            myGateservo1.setPosition(0.33);
            sleep(2000);
            myCRservo1.setPower(-1);
            myCRservo2.setPower(-1);
            Intake.setPower(-1);
            sleep(1000);
            Launch1.setPower(feedforward.calculate(4.6));
            myCRservo1.setPower(-1);
            myCRservo2.setPower(-1);
            Intake.setPower(-1);
            sleep(500);
            Launch1.setPower(feedforward.calculate(4.3));
            myCRservo1.setPower(-1);
            myCRservo2.setPower(-1);
            Intake.setPower(-1);
            //Launch1.setPower(0);
            telemetry.addData("Launch1",Launch1.getCurrentPosition());
            telemetry.update();
            sleep(6000);
            forward(0.5, -750);
            sleeep(1000);
            strafe(1, 1000);
            sleeep(1000);
            /*turn(0.5, 375);
            sleeep(500);
            forward(0.5, -500);
            sleeep(500);
            turn(0.5, -750);
            sleeep(1000);
            Intake.setPower(-1);
            forward(0.5, -1000);
            sleeep(800);
            forward(0.5, 1000)
            sleeep(800);
            Intake.setPower(0)
            turn(0.5, 750)
            sleeep(1000);
            forward(0.5, 500);
            sleeep(500);
            turn(0.5, 375);
            sleeep(500);
            //Insert PID code here :)
            strafe(0.5, 1000)
            */

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
        //Launch1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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