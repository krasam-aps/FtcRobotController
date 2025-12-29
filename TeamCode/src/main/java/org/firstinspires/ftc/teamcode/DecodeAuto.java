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
    CRServo myCRservo1;
    CRServo myCRservo2;
    //Servo myGateservo1;
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
        //myGateservo1 = hardwareMap.get(Servo.class, "myGateservo1");

        //myGateservo1.scaleRange(-1.0, 1.0);

        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);

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
            turn(0.9, -170);
            sleep(1000);
            resetEncoder();
            //launches big bouncy balls (def not  with holes towards goal
            Launch1.setPower(.67);
            sleep(5000);

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
        myCRservo1.setPower(power);
        myCRservo2.setPower(power);
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