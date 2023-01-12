package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;

@TeleOp
public class Auton1 extends LinearOpMode {
    // Declare our motors
    // Make sure your ID's match your configuration
    DcMotor BLeft, BRight, FLeft, FRight, Slider;
    //CRServo spinner;
    Servo Take1, Take2;
    //double pwr = 0.2;
    FindYellowTarget vision = new FindYellowTarget();

    @Override
    public void runOpMode() {
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        rightIntakeSlapper = hardwareMap.get(Servo.class, "rightIntakeSlapper");
        leftIntakeSlapper = hardwareMap.get(Servo.class, "leftIntakeSlapper");
        rotator = hardwareMap.get(Servo.class, "rotator");
        releaser = hardwareMap.get(Servo.class, "releaser");
        spinner = hardwareMap.get(CRServo.class, "spinner");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake = hardwareMap.dcMotor.get("leftIntake");
        extender = hardwareMap.dcMotor.get("extender");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            toggleIntakeOn();
            moveRight();
            sleep(3500);

            toggleIntakeOff();
            stopDrivebase();
            sleep(1000);

            toggleSweeper();
            sleep(1000);

            toggleReleaserClose();
            sleep(1000);

            rotateArm();
            sleep(3000);
            toggleReleaserOpen();

        }
    }



    //move forward function
    public void moveRight() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(pwr);
    }

    public void moveLeft() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(pwr);
        motorBackLeft.setPower(pwr);
        motorBackRight.setPower(-pwr);
    }

    public void moveForward() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(pwr);
    }

    public void moveBackward() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(-pwr);
    }

    public void stopDrivebase() {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public void turnLeft() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(pwr);
        motorBackLeft.setPower(pwr);
        motorBackRight.setPower(pwr);
    }

    public void toggleIntakeOn() {
        rightIntake.setPower(0.8);
    }

    public void toggleIntakeOff() {
        rightIntake.setPower(0);
    }

    public void toggleSweeper() {
        rightIntakeSlapper.setPosition(0.6);
        sleep(500);
        rightIntakeSlapper.setPosition(0);
    }

    public void toggleReleaserClose() {
        releaser.setPosition(0.7);
    }

    public void toggleReleaserOpen() {
        releaser.setPosition(1);
    }

    public void rotateArm() {
        rotator.setPosition(0.8);
    }



}