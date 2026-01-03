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

@Autonomous(name = "Far Launch Red Goal", group = "Autonomous")
public class FarLaunchRedGoal extends LinearOpMode {

    // Power used to be 56!
    // Hardware Members
    DcMotor Fleft, Fright, Bleft, Bright;
    DcMotor Intake, Bottom_Launch, Top_Launch, SpinDexer;
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
            double powerRating = feedforward.calculate(3.4);

            // Drive Forward
            forward(0.5, 450);

            // Turn roughly towards goal (Pre-alignment)
            resetEncoder();// Update PID creates a holding force on SpinDexer if it was moving,
            // but here we just ensure systems are active.
            turn(0.5, -230);

            // SPIN UP FLYWHEEL
            Bottom_Launch.setPower(0.53);
            Top_Launch.setPower(0.53);
            telemetry.addData("Shooter", "Spinning Up");
            telemetry.update();

            // Wait for flywheel speed (maintaining SpinDexer hold)
            sleeep(3000);

            // LIMELIGHT AUTO-AIM
            // This will take control of the wheels to center on the target
            //alignWithLimelight(3.0); // 3 second timeout

            // SHOOTING SEQUENCE (3 Balls)
            // Shot 1
            Kicker(1, 300);
            sleeep(300);

            // Index Next Ball
            SpinDexer(0.8, TICKS_PER_CHAMBER);
            sleeep(300);

            // Shot 2
            Kicker(1, 300);
            sleeep(300);

            // Index Next Ball
            SpinDexer(0.8, TICKS_PER_CHAMBER);
            sleeep(300);

            // Shot 3
            Kicker(1, 300);
            sleeep(500);

            // Reset SpinDexer
            SpinDexer(0.8, 376); // Slight extra rotation to clear
            sleeep(300);

            // Shutdown
            Bottom_Launch.setPower(0);
            Top_Launch.setPower(0);
            //limelight.stop();

            telemetry.addData("Status", "Strafing to Park");
            telemetry.update();
            strafe(0.8, 1000);
        }
    }

    /*
      Aligns the robot using Limelight vision.
      Includes a timeout and keeps the SpinDexer PID active.
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
            updateSpinDexerPID(1.0);

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

    public void strafe(double power, int ticks) {
        // Strafe Right: FL(+), FR(-), BL(-), BR(+)
        // Strafe Left:  FL(-), FR(+), BL(+), BR(-)

        Fleft.setTargetPosition(Fleft.getCurrentPosition() + ticks);
        Fright.setTargetPosition(Fright.getCurrentPosition() - ticks);
        Bleft.setTargetPosition(Bleft.getCurrentPosition() - ticks);
        Bright.setTargetPosition(Bright.getCurrentPosition() + ticks);

        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(Math.abs(power));
        waitForMove();
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

    // Helper to block execution until motors reach target
    // ALSO updates SpinDexer PID so the shooter doesn't drift
    public void waitForMove() {
        while (opModeIsActive() &&
                (Fleft.isBusy() && Fright.isBusy() && Bleft.isBusy() && Bright.isBusy())) {
            updateSpinDexerPID(1.0);
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

    public void Kicker(double power, long milliseconds) {
        // Kick
        LeftKicker.setPosition(0.5);
        RightKicker.setPosition(0.5);
        sleeep(milliseconds);
        // Retract
        LeftKicker.setPosition(0.25);
        RightKicker.setPosition(0.25);
    }

    public void SpinDexer(double power, int ticks) {
        spinTargetPosition += ticks;
        ElapsedTime timeout = new ElapsedTime();
        // Wait until close to target or timeout (2 seconds)
        while (opModeIsActive() &&
                Math.abs(spinTargetPosition - SpinDexer.getCurrentPosition()) > 15 &&
                timeout.seconds() < 2) {
            updateSpinDexerPID(power);
        }
        // Maintain position (don't set power to 0, let PID hold it in sleeep calls)
    }

    public void updateSpinDexerPID(double maxPowerLimit) {
        double currentPos = SpinDexer.getCurrentPosition();
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
            SpinDexer.setPower(pidPower);
            lastError = error;
        }
    }

    public void sleeep(long milliseconds) {
        ElapsedTime waitTimer = new ElapsedTime();
        while (opModeIsActive() && waitTimer.milliseconds() < milliseconds) {
            updateSpinDexerPID(1.0); // Hold SpinDexer position
        }
    }

    public void initializeHardware() {
        Fleft = hardwareMap.get(DcMotor.class, "Fleft");
        Fright = hardwareMap.get(DcMotor.class, "Fright");
        Bleft = hardwareMap.get(DcMotor.class, "Bleft");
        Bright = hardwareMap.get(DcMotor.class, "Bright");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Bottom_Launch = hardwareMap.get(DcMotor.class, "Bottom_Launch");
        Top_Launch = hardwareMap.get(DcMotor.class, "Top_Launch");
        SpinDexer = hardwareMap.get(DcMotor.class, "SpinDexer");
        RightKicker = hardwareMap.get(Servo.class, "RK");
        LeftKicker = hardwareMap.get(Servo.class, "LK");

        // Limelight Init
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0); // Ensure using Pipeline 0

        // Motor Directions
        Fleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Bleft.setDirection(DcMotorSimple.Direction.REVERSE);
        Top_Launch.setDirection(DcMotorSimple.Direction.FORWARD);
        Bottom_Launch.setDirection(DcMotorSimple.Direction.REVERSE);
        RightKicker.setDirection(Servo.Direction.REVERSE);
        LeftKicker.setDirection(Servo.Direction.FORWARD);

        // Encoder Setup
        Fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpinDexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Bottom_Launch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Top_Launch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        SpinDexer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Zero Power Behavior
        Fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}