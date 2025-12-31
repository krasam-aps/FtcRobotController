package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "SpindexerTelemetry2", group = "Linear OpMode")
public class SpindexerTelemetry2 extends LinearOpMode {
    // Hardware
    DcMotor SpinDexer;
    @Override
    public void runOpMode() {
        // Initialization
        SpinDexer = hardwareMap.get(DcMotor.class, "Spindexer");

        // Reversed Spindexer so encoder and power directions match
        SpinDexer.setDirection(DcMotorSimple.Direction.REVERSE);
        SpinDexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpinDexer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Spindexer Pos:", SpinDexer.getCurrentPosition());
            telemetry.update();
            telemetry.update();
        }
    }
}