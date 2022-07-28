package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class OpMode1 extends LinearOpMode {

    @Override
    public void runOpMode()  {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("backRight");
        CRServo spinner;
        spinner = hardwareMap.get(CRServo.class, "spinner");
        DcMotor rightIntake = hardwareMap.dcMotor.get("rightIntake");
        DcMotor leftIntake = hardwareMap.dcMotor.get("leftIntake");
        Servo rightIntakeSlapper;
        rightIntakeSlapper = hardwareMap.get(Servo.class, "rightIntakeSlapper");
        Servo leftIntakeSlapper;
        leftIntakeSlapper = hardwareMap.get(Servo.class, "leftIntakeSlapper");
        DcMotor extender = hardwareMap.dcMotor.get("extender");
        Servo rotator;
        rotator = hardwareMap.get(Servo.class, "rotator");
        Servo releaser;
        releaser = hardwareMap.get(Servo.class, "releaser");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        //drivebase code
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            //drivebase code


            //Duckie wheel code
            if (gamepad1.a) {
                spinner.setPower(0.8);
            } else if (gamepad1.b) {
                spinner.setPower(-0.8);
            } else {
                spinner.setPower(0);
            }
            //Duckie wheel code


            //Intake code
            if (gamepad1.right_bumper) {
                rightIntake.setPower(0.8);
            } else rightIntake.setPower(0);

            if (gamepad1.left_bumper) {
                leftIntake.setPower(-0.8);
                System.out.println("Intake works");
                telemetry.addData("Intake", "working");
                telemetry.update();
            } else leftIntake.setPower(0);
            //Intake code


            //sweeper code
            if (gamepad1.y) {
                rightIntakeSlapper.setPosition(0.45);
                telemetry.addData("right sweeper", "working");
                telemetry.update();
            } else rightIntakeSlapper.setPosition(0);

            if (gamepad1.x) {
                leftIntakeSlapper.setPosition(0.45);
                telemetry.addData("left sweeper", "working");
                telemetry.update();
            } else leftIntakeSlapper.setPosition(0);
            //sweeper code

            //extender code
            if (gamepad1.right_trigger > 0) {
                extender.setPower(-0.6);
            } else extender.setPower(0);

            if (gamepad1.left_trigger > 0) {
                extender.setPower(0.6);
            } else extender.setPower(0);
            //extender code

            //rotator code
            if (gamepad1.dpad_up) {
                rotator.setPosition(0.8);
                telemetry.addData("rotator", "working");
                telemetry.update();
            }else rotator.setPosition(0.3);
            //rotator code

            //grabber code
            if (gamepad1.dpad_down) {
                releaser.setPosition(0.75);
                telemetry.addData("releaser", "working");
                telemetry.update();
            }else releaser.setPosition(1);
            //grabber code

        }
    }}