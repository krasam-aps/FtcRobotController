package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
//import com.pedropathing.follower.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// plz fix gradle
@Autonomous
public class OldToesTasteSweet extends LinearOpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    // Specific hardware
    private DcMotor Launch1;
    private Servo myGateservo1;

    // Define coordinates (Field is 144x144 inches)
    private final Pose startPose = new Pose(56, 8, Math.toRadians(0));
    private final Pose endPose = new Pose(56, 41, Math.toRadians(0));

    private PathChain forward;
    private PathChain backward;

    @Override
    public void runOpMode() {
        pathTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        // Initialize the mechanism hardware
        Launch1 = hardwareMap.get(DcMotor.class, "Launch1");
        myGateservo1 = hardwareMap.get(Servo.class, "myGateservo1");
        myGateservo1.setPosition(1.0); // Close gate
        // I now nono sane :(
//                      I hate snakeCase (put into camelCase as a scorn)! \\

        buildPaths();

        telemetry.addData("Status", "Initialized with OTOS");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        setPathState(0);

        while (opModeIsActive() && !isStopRequested()) {
            follower.update();
            autonomousPathUpdate();

            // Feedback
            telemetry.addData("Path State", pathState);
            telemetry.addData("Position", follower.getPose().toString());
            //follower.telemetryDebug(telemetry);
            telemetry.update();
        }
    }

    public void buildPaths() {
        // Path to go score
        /*scorePath = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();*/
        forward = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(270))
                .build();


        backward = follower.pathBuilder()
                .addPath(new BezierLine(endPose, startPose))
                .setLinearHeadingInterpolation(endPose.getHeading(), startPose.getHeading())
                .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(90))
                .build();

        //     \/
        //      v
        //

        // Path to park
        /*parkPath = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(60, 100), new Point(parkPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();*/
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Move
                follower.followPath(forward);
                setPathState(1);
                break;

            case 1: // Move
                follower.followPath(backward);
                setPathState(2);
                break;

            /*case 1: // Wait until robot reaches scoring position
                if (!follower.isBusy()) {
                    Launch1.setPower(0.58); // Spin up shooter
                    if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                        myGateservo1.setPosition(0.44); // Open gate
                        setPathState(2);
                        pathTimer.resetTimer();
                    }
                } else {
                    pathTimer.resetTimer();
                }
                break;

            case 2: // Wait for shot then move to park
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    Launch1.setPower(0);
                    myGateservo1.setPosition(1.0);
                    follower.followPath(parkPath);
                    setPathState(3);
                }
                break;*/

            case 2: // Finished
                if (!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }
}