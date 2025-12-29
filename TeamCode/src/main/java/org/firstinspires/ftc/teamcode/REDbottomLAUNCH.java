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
//         \   /      /  /\  \      |  |        /    /        |  |     |   |
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
public class REDbottomLAUNCH extends LinearOpMode {
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
    snake_Case - Not okay, you need HELP if u use this
    Dot.Case - Truce made between Krish & CJ, it is mid.
    myYatzilCase - made by Yatzil, and basically SUCKS (even CJ agrees so)
    gLiTcH.cAsE - A joke, made by Krish. He uses every once in a while/
    CaseQuat - A version of an Evil Kumquat, except made for Java (like ExampleQuat or teleOpjavaQuat)
               For more information, visit the Scratch Forums, or this one specifically: **in the process of inserting link**
    casecase - CJ's new case. Weird, just like him >:)
    PascalCase - Upper camelCase. It's fune, but it is worse than the OG camelCase
    SCREAMING_SNAKE_CASE - SNAKE_CASE, BUT ALL CAPS. GOOGLE TOLD ME THIS ONE, I DON'T LIKE IT
                           IT IS WEIRDER AND YOU NEED MORE HELP IF YOU USE THIS CASE!!!!!!!!!
    kebab-case - It's words shish-kebabed on a stick. They don't taste good, tho.
    Train-Case - A train of words! Not very fast, and the only people that can ride it are
                 4 foot 7, just like CJ!

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
        Intake.setDirection(DcMotorSimple.Direction.FORWARD);
        Launch1.setDirection(DcMotorSimple.Direction.FORWARD);
        myCRservo1.setDirection(DcMotorSimple.Direction.REVERSE);
        myCRservo2.setDirection(DcMotorSimple.Direction.REVERSE);

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
            myGateservo1.setPosition(1);
            resetEncoder();
            forward(0.5, 175);
            sleeep(300);// Previously 1000 milliseconds
            //turn towards red goal
            turn(0.5, -180);
            sleeep(300);//Previously 1000 milliseconds
            //LAUNCH FROM FAR LAUNCH AREAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            launchWintake(0.62, 40000);
            sleeep(2000);
            turn(0.5, 170);
            sleeep(300);//Previously 1000 milliseconds
            forward(0.5, 600);
            sleeep(750);
            turn(0.5, 750); //Note to self - exact ticks needed for a 90 degree angle is 750. Sometimes it doesn't work. I wonder why...
            sleeep(1000);
            backwardWintake(0.5, 1800);
            sleeep(1800);
            forward(0.5, 1800);
            sleeep(1200);
            turn(0.5, -750);
            STOPtransferINTAKE();
            sleeep(1000);
            forward(0.5, -600);
            sleeep(750);
            turn(0.5, -170);
            sleeep(300);//Previously 1000 milliseconds
            launchWintake(0.62, 40000);
            sleeep(2000);
        }
    }

    //              ___
    //             |o-o| hi im bob
    //              |-|
    // this is bob, the protector of the methods
    // he came from the gradle demon
    // pray to him, and he will spare you
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
    public void STOPtransferINTAKE(){
        myCRservo1.setPower(0);
        myCRservo2.setPower(0);
        Intake.setPower(0);
    }
    public void CRservo(double power){
        myCRservo1.setPower(power);
        myCRservo1.setPower(power);
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
    public void launchWintake(double power, int ticks){
        Launch1.setTargetPosition(ticks);

        Launch1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        myCRservo1.setPower(0);
        myCRservo2.setPower(0);
        Intake.setPower(0);
        Launch1.setPower(power);
        sleep(2500);
        myGateservo1.setPosition(0.4);
        sleep(1000);
        Launch1.setPower(power);
        myCRservo1.setPower(1);
        myCRservo2.setPower(1);
        Intake.setPower(1);
        sleep(4000);
        Launch1.setPower(power);
        myCRservo1.setPower(1);
        myCRservo2.setPower(1);
        Intake.setPower(1);
        sleep(1000);
        myCRservo1.setPower(0);
        myCRservo2.setPower(0);
        Intake.setPower(0);
        myGateservo1.setPosition(1);
        sleep(1000);


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