package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;




@TeleOp
public class MovementCV extends LinearOpMode {
    // Declare our motors
    // Make sure your ID's match your configuration
    DcMotor motorFrontLeft, motorBackLeft, motorFrontRight, motorBackRight, rightIntake, leftIntake, extender;
    CRServo spinner;
    Servo rightIntakeSlapper, leftIntakeSlapper;
    Servo rotator, releaser;
    double pwr = 0.1;
    FindYellowTarget vision = new FindYellowTarget();
    int state = 0;
    int range = 20;


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

        OpenCvCamera camera;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
        camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        camera.setPipeline(vision);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(160, 120, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode)
            {

                // This will be called if the camera could not be opened

            }
        });

        waitForStart();

        while (opModeIsActive()) {

            int TargetNumber = vision.getTargetCount();
            int X = vision.getTargetX();

            if ((state == 1)){
                collect();
            }else
                seeking();

            telemetry.addData("STATE:", state);
            telemetry.addData("X", X);
            telemetry.update();



        }
    }



    public void moveRight() {
        motorFrontLeft.setPower(0.15);
        motorFrontRight.setPower(-0.15);
        motorBackLeft.setPower(-0.15);
        motorBackRight.setPower(0.15);
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

    public void turnRight() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(pwr);
        motorBackRight.setPower(-pwr);
    }

    public void turnLeft() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(pwr);
    }

    public void toggleIntakeOn() {
        rightIntake.setPower(0.8);
    }

    public void toggleIntakeOff() {
        rightIntake.setPower(0);
    }

    public void toggleSweeper() {
        rightIntakeSlapper.setPosition(0.45);
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

    public void seeking() {
        int TargetNumber = vision.getTargetCount();
        int X = vision.getTargetX();


        //Positioning
        if((TargetNumber > 0) && (X - 80) > range){
            turnRight();
            toggleIntakeOff();
            telemetry.addData("Turning Right!!!", -1000);
            telemetry.addData("Target Count:", TargetNumber);
            telemetry.addData("Target X", X);


        } else if ((TargetNumber > 0) && (X - 80) < -range){
            turnLeft();
            toggleIntakeOff();
            telemetry.addData("Turning Left!!!", -1000);
            telemetry.addData("Target Count:", TargetNumber);
            telemetry.addData("Target X", X);


        } else if ((TargetNumber > 0) && (85 > X) && (X > 75)){
            telemetry.addData("Target Count:", TargetNumber);
            telemetry.addData("Target X", X);
            state = 1;
            toggleIntakeOn();


        }else toggleIntakeOff();


        //
        if (TargetNumber > 0){
            telemetry.addData("Target Found!!!", TargetNumber);
            telemetry.addData("Target X", X);


            telemetry.update();
        } else {
            turnLeft();
            telemetry.addData("Target Not Found", -1);
            telemetry.update();
        }}


        public void collect() {
        int TargetNumber = vision.getTargetCount();
        int X = vision.getTargetX();

            if (((TargetNumber > 0) && ((80 + range) > X) && (X > (80 - range)))){
            toggleIntakeOn();
            moveRight();
            telemetry.addData("COLLECT IS ON", -1000);
            telemetry.update();}
        else state = 0;
        }


}