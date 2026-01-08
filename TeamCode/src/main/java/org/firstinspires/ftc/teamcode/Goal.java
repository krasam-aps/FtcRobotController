package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

@Autonomous(name = "Goal", group = "Autonomous")
public class Goal extends LinearOpMode {
    //ODD TOE BOTS, ASSEMBLE
    // Hardware Members
    DcMotor Fleft, Fright, Bleft, Bright;
    DcMotor Intake, Launch1, Launch2, Spindexer;
    Servo RightKicker, LeftKicker;
    Limelight3A limelight;

    // Constants & PID Variables
    int spinTargetPosition = 0;
    int TICKS_PER_CHAMBER = 251;

    double Kp = 0.005;
    double Ki = 0.0;
    double Kd = 0.0001;
    double integralSum = 0;
    double lastError = 0;
    ElapsedTime pidTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        // INITIALIZATION
        initializeHardware();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Limelight", "Connected");
        telemetry.update();

        waitForStart();
        pidTimer.reset();

        // Start Limelight Streaming
        //limelight.start();

        if (opModeIsActive()) {
            resetEncoder();

            // DRIVE TO SHOOTING POSITION
            // Calculate Feedforward for Shooter
            SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0, 0.1125, 0.27);
            double powerRating = feedforward.calculate(4.5);

            // Drive Backwards
            forward(0.5, -1200);

            // SPIN UP FLYWHEEL
            Launch1.setPower(powerRating);
            Launch2.setPower(powerRating);
            telemetry.addData("Shooter", "Spinning Up");
            telemetry.update();

            // Wait for flywheel speed (maintaining Spindexer hold)
            sleeep(2500);

            // LIMELIGHT AUTO-AIM
            // This will take control of the wheels to center on the target
            //alignWithLimelight(3.0); // 3 second timeout

            // SHOOTING SEQUENCE (3 Balls)
            // Shot 1
            Kicker(1, 300);
            sleeep(300);

            // Index Next Ball
            Spindexer(0.65, TICKS_PER_CHAMBER);
            sleeep(300);

            // Shot 2
            Kicker(1, 300);
            sleeep(300);

            // Index Next Ball
            Spindexer(0.65, TICKS_PER_CHAMBER);
            sleeep(300);

            // Shot 3
            Kicker(1, 300);
            sleeep(500);

            // Reset Spindexer
            Spindexer(0.65, 376); // Slight extra rotation to clear
            sleeep(300);

            // Shutdown
            Launch1.setPower(0);
            Launch2.setPower(0);
            //limelight.stop();

            //STRAFE
            strafe(1, 1000);
            sleeep(1000);
        }
    }

    /*
      Aligns the robot using Limelight vision.
      Includes a timeout and keeps the Spindexer PID active.
     */
    public void alignWithLimelight(double timeoutSeconds) {
        ElapsedTime timeout = new ElapsedTime();
        double targetTolerance = 1.0;
        double kP_Aim = 0.035; // PID proportional gain for turning

        // Switch to run without encoder for smooth speed control during aiming
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Auto-Aim", "Started");
        telemetry.update();

        while (opModeIsActive() && timeout.seconds() < timeoutSeconds) {
            // Keep the spindexer locked/moving even while aiming
            updateSpindexerPID(1.0);

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double tx = result.getTx(); // Horizontal Error

                if (Math.abs(tx) > targetTolerance) {
                    double turnPower = tx * kP_Aim;

                    // Clamp power to prevent stalling or overshooting
                    double minPower = 0.12;
                    double maxPower = 0.4;

                    if (Math.abs(turnPower) < minPower) turnPower = Math.signum(turnPower) * minPower;
                    if (Math.abs(turnPower) > maxPower) turnPower = Math.signum(turnPower) * maxPower;

                    // Apply rotation (Left +, Right - for Clockwise turn if tx is positive)
                    Fleft.setPower(turnPower);
                    Bleft.setPower(turnPower);
                    Fright.setPower(-turnPower);
                    Bright.setPower(-turnPower);

                    telemetry.addData("Aiming", "tx: %.2f | Power: %.2f", tx, turnPower);
                } else {
                    // Aligned
                    stopDriveMotors();
                    telemetry.addData("Aiming", "Aligned!");
                    telemetry.update();
                    break;
                }
            } else {
                // No target seen, stop to be safe
                stopDriveMotors();
            }
            telemetry.update();
        }

        stopDriveMotors();
        // Reset encoders for next move commands
        resetEncoder();
    }

    public void forward(double power, int ticks) {
        setTargetPositionAll(ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(Math.abs(power));
        waitForMove();
    }

    public void turn(double power, int ticks) {
        // Turning logic: Left -, Right + (for Right Turn) OR Left +, Right - (for Left Turn)
        // Based on your original code:
        // FL(-), FR(+), BL(-), BR(+)
        Fleft.setTargetPosition(-ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(-ticks);
        Bright.setTargetPosition(ticks);

        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(Math.abs(power));
        waitForMove();
    }

    // Helper to block execution until motors reach target
    // ALSO updates Spindexer PID so the shooter doesn't drift
    public void waitForMove() {
        while (opModeIsActive() &&
                (Fleft.isBusy() && Fright.isBusy() && Bleft.isBusy() && Bright.isBusy())) {
            updateSpindexerPID(1.0);
        }
        stopDriveMotors();
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setTargetPositionAll(int ticks) {
        Fleft.setTargetPosition(ticks);
        Fright.setTargetPosition(ticks);
        Bleft.setTargetPosition(ticks);
        Bright.setTargetPosition(ticks);
    }

    public void setRunMode(DcMotor.RunMode mode) {
        Fleft.setMode(mode);
        Fright.setMode(mode);
        Bleft.setMode(mode);
        Bright.setMode(mode);
    }

    public void setPowerAll(double power) {
        Fleft.setPower(power);
        Fright.setPower(power);
        Bleft.setPower(power);
        Bright.setPower(power);
    }

    public void stopDriveMotors() {
        setPowerAll(0);
    }

    public void resetEncoder() {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public void Kicker(double power, long milliseconds) {
        // Kick
        LeftKicker.setPosition(0.5);
        RightKicker.setPosition(0.5);
        sleeep(milliseconds);
        // Retract
        LeftKicker.setPosition(0.25);
        RightKicker.setPosition(0.25);
    }

    public void Spindexer(double power, int ticks) {
        spinTargetPosition += ticks;
        ElapsedTime timeout = new ElapsedTime();
        // Wait until close to target or timeout (2 seconds)
        while (opModeIsActive() &&
                Math.abs(spinTargetPosition - Spindexer.getCurrentPosition()) > 15 &&
                timeout.seconds() < 2) {
            updateSpindexerPID(power);
        }
        // Maintain position (don't set power to 0, let PID hold it in sleeep calls)
    }

    public void updateSpindexerPID(double maxPowerLimit) {
        double currentPos = Spindexer.getCurrentPosition();
        double error = spinTargetPosition - currentPos;
        double dt = pidTimer.seconds();
        pidTimer.reset();

        // PID Logic
        if (dt > 0) {
            integralSum += error * dt;
            // Anti-windup
            integralSum = Math.max(-100, Math.min(100, integralSum));

            double derivative = (error - lastError) / dt;
            double pidPower = (error * Kp) + (integralSum * Ki) + (derivative * Kd);

            // Friction Feedforward / Min Power
            if (Math.abs(error) > 10) {
                if (Math.abs(pidPower) < 0.15) {
                    pidPower = Math.signum(error) * 0.15;
                }
            } else {
                pidPower = 0; // Stop if within tolerance
                integralSum = 0;
            }

            // Clamp result
            pidPower = Math.max(-maxPowerLimit, Math.min(maxPowerLimit, pidPower));
            Spindexer.setPower(pidPower);
            lastError = error;
        }
    }

    public void sleeep(long milliseconds) {
        ElapsedTime waitTimer = new ElapsedTime();
        while (opModeIsActive() && waitTimer.milliseconds() < milliseconds) {
            updateSpindexerPID(1.0); // Hold Spindexer position
        }
        resetEncoder();
    }

    public void initializeHardware() {
        Fleft = hardwareMap.get(DcMotor.class, "Fleft");
        Fright = hardwareMap.get(DcMotor.class, "Fright");
        Bleft = hardwareMap.get(DcMotor.class, "Bleft");
        Bright = hardwareMap.get(DcMotor.class, "Bright");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Launch1 = hardwareMap.get(DcMotor.class, "Bottom_Launch");
        Launch2 = hardwareMap.get(DcMotor.class, "Top_Launch");
        Spindexer = hardwareMap.get(DcMotor.class, "SpinDexer");
        RightKicker = hardwareMap.get(Servo.class, "RK");
        LeftKicker = hardwareMap.get(Servo.class, "LK");

        // Limelight Init
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0); // Ensure using Pipeline 0

        // Motor Directions
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Launch2.setDirection(DcMotorSimple.Direction.FORWARD);
        Launch1.setDirection(DcMotorSimple.Direction.REVERSE);
        RightKicker.setDirection(Servo.Direction.REVERSE);
        LeftKicker.setDirection(Servo.Direction.FORWARD);

        // Encoder Setup
        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Spindexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Launch1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Launch2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Spindexer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Zero Power Behavior
        Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}