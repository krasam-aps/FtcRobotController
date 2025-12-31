//      |---|  /---/  |---====--\  |----------|   /---------|  |---|   |---|         |---====--\
//      |   | /   /   |   |  |  |  |---    ---|  |          /  |   |   |   |         |   |  |  |
//      |   |/   /    |   '--'  /      |  |      |    -----    |   |---|   |         |   '--'  /
//      |       |     |       \        |  |       \        \   |           |         |       \
//      |   |\   \    |   |\   \       |  |        -----    |  |   |---|   |         |   |\   \
//      |   | \   \   |   | \   \  |---    ---|  /          |  |   |   |   |  |---|  |   | \   \
//      |---|  \---\  |---|  \---\ |----------|  |---------/   |---|   |---|  |---|  |---|  \---\
//
// DISCLAMER!!!!!!!!
// code may have certain cheeses, murder scenes, necromancy, cursed code, and bob

package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class DecodeAuto extends LinearOpMode {
    DcMotor Fleft = null;
    DcMotor Fright = null;
    DcMotor Bleft = null;
    DcMotor Bright = null;
    DcMotor Intake;
    DcMotor Launch1;
    DcMotor Launch2;
    DcMotor Spindexer;
    Servo launchServo;
    //CRServo myCRservo1;
    //CRServo myCRservo2;
    //Servo myGateservo1;
    double power = 1.0;

    public void runOpMode(){
        Fleft = hardwareMap.get(DcMotor.class,"Fleft");
        Fright = hardwareMap.get(DcMotor.class,"Fright");
        Bleft = hardwareMap.get(DcMotor.class,"Bleft");
        Bright = hardwareMap.get(DcMotor.class,"Bright");
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Launch1 = hardwareMap.get(DcMotor.class,"Launch1");
        Launch2 = hardwareMap.get(DcMotor.class,"Launch2");
        Spindexer = hardwareMap.get(DcMotor.class,"Spindexer");
        launchServo = hardwareMap.get(Servo.class,"RightKicker");

        //myGateservo1 = hardwareMap.get(Servo.class, "myGateservo1");

        //myGateservo1.scaleRange(-1.0, 1.0);

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Launch1.setDirection(DcMotorSimple.Direction.FORWARD);
        Launch2.setDirection(DcMotorSimple.Direction.REVERSE);

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Launch1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Launch2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();
        /* krish log of amazing things
                the code is cursed
                the code may not be cursed
                compiling cha cha!!
                the code is def cursed
                tbh, snake_case prob cursed my code, so i cursed his
                the code is the god of hell, hades
                the code is amazing!!
                oh cheddar this jackhole is a big fat mozzarela
                big hump of cursed code uuguhh CHEDDARRR
                (necromancy chanting starts) ohhh ehhh aaahh body gobity dooooo
                guys the murderd bot's light became blue!!! and there is NO BATTERY!!
         */
        if (opModeIsActive()) {

            resetEncoder();
            forward(0.5, 150);
            sleep(1000);
            resetEncoder();
            //turn towards red goal
            turn(0.5, -140);
            sleeep(300);
            sleeep(2000); //This simulates the time it takes to launch
            turn(0.5, 140);
            sleeep(300);//Previously 1000 milliseconds
            forward(0.5, 580);
            sleeep(750);
            turn(0.5,726);
            sleeep(1000);
            backwardWintake(0.5, 1000);
            sleeep(2000);
            forward(0.5,1000);
            Intake.setPower(0);
            sleeep(2000);
            turn(0.5,-726);
            sleeep(1000);
            forward(0.5, -570);
            sleeep(750);
            turn(0.5, -140);
            sleeep(300);
            sleeep(2000); //Simulates launch
            turn(0.5, 280);
            sleeep(300);
            forward (1, 1000);
            sleeep(1000);
            /*forward(0.5, 1000);
            sleeep(1000);
            forward(0.5, -1000);
            sleeep(1000);
            strafe(1, -1000);
            sleeep(100);*/


            /*turn(0.5, 750); //Note to self - exact ticks needed for a 90 degree angle is 750. Sometimes it doesn't work. I wonder why...
            sleeep(1000);
            backwardWintake(0.5, 1800);
            sleeep(1800);
            forward(0.5, 1800);
            sleeep(1200);
            turn(0.5, -750);
            sleeep(1000);
            forward(0.5, -600);
            sleeep(750);
            turn(0.5, -170);*/


            //launches big bouncy balls with holes towards goal

            /*
            // Create a new SimpleMotorFeedforward with gains kS, kV, and kA
                  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(kS, kV, kA);

            // Calculates the feedforward for a velocity of 10 units/second
            // and an acceleration of 20 units/second^2
            // Units are determined by the units of the gains passed
            // in at construction.
                   feedforward.calculate(10, 20);
             */
            /*telemetry.addData("Launch1",Launch1.getCurrentPosition());
            telemetry.update();
            //sleeep(2500)
            // 0, .146, 0.277

            SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 0.1125, 0.27); //ka=.27
            // 4
            double powerRating = feedforward.calculate(3.4);
            //double powerRating2 = -1*(feedforward.calculate(3.4));
            telemetry.addData("Launch1 powerRating: ", powerRating);
            telemetry.update();
            Launch1.setPower(powerRating);
            sleep(1000);
            Launch1.setPower(feedforward.calculate(4.6));
            sleep(500);
            Launch1.setPower(feedforward.calculate(4.3));
            sleep(2000);
            Launch1.setPower(0);*/


            /*//turn to 90 degreese
            turn(0.9, 882);
            sleep(1000);
            resetEncoder();
            // strafe to first spike w/ balls
            strafe(0.9, 800);
            sleep(2000);
            resetEncoder();
            // collect balls on 1st spike
            backwardWintake(0.75, 1000);
            sleep(1200);
            resetEncoder();
            //go back to position before collecting
            forward(0.75, 1000);
            sleep(1200);
            resetEncoder();
            // go back to 90 degree position after launching
            strafe(0.9, -800);
            sleep(2000);
            resetEncoder();
            // turn towards goal
            turn(0.9, -882);
            sleep(1000);
            resetEncoder();
            //lAuNcH1!1!1!
            launchWintake(0.9, 750);
            sleep(2000);
            // back to 90 degree
            turn(0.9, 882);
            sleep(1000);
            resetEncoder();
            //strafe to 2nd spike
            strafe(0.9, 1700);
            sleep(2000);
            resetEncoder();
            // suck up balls
            backwardWintake(0.75,1000);
            sleep(1200);
            resetEncoder();
            // go back to pos. before sucking up balls
            forward(0.75, 1000);
            sleep(1200);
            resetEncoder();
            // go back to 90 degree pos
            strafe(0.9, -1700);
            sleep(2000);
            resetEncoder();
            //turn towards goal
            turn(0.9, -882);
            sleep(1000);
            resetEncoder();
            //lAaAaUuUuNnNnCcCcHhHhHhHhHh!!!!!!!
            launchWintake(1,750);
            sleep(2000);
            resetEncoder();
            // turn towards murdered bot (murdered by snake_case)
            turn(0.9,1000);
            sleep(1200);
            resetEncoder();
            // push dead bot (rest in peace, comrade)
            forward(0.9, -1000);
            sleep(1200);
            resetEncoder();

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

    //-strafe is strafeLeft, strafe is strafeRight
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

    public void forwardV2(double power, int ticks){
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
        //myCRservo1.setPower(power);
        //myCRservo2.setPower(power);
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
    public void Flywheel(double power, int time){

        Launch1.setPower(1);
        sleep(time);
        Launch1.setPower(0);
        //myGateservo1.setPosition(0);

    }
    public void Intake(double power, int ticks){
        Intake.setTargetPosition(ticks);

        Intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Intake.setPower(power);
        //myCRservo1.setPower(power);
        //myCRservo2.setPower(power);
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