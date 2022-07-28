package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.TimestampedOpenCvPipeline;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.imgproc.Moments;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;






@TeleOp
public class OpenCVTest extends LinearOpMode
{
    OpenCvCamera camera;
    FindYellowTarget vision = new FindYellowTarget();


    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
        camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        camera.setPipeline(new FindYellowTarget());
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);
                //camera.set(3, 320);
                //camera.set(4, 240);
            }

            @Override
            public void onError(int errorCode)
            {

                 // This will be called if the camera could not be opened

            }
        });

        telemetry.setMsTransmissionInterval(50);
        telemetry.addLine("Waiting for start");
        telemetry.update();



         //Wait for the user to press start on the Driver Station

        waitForStart();

        while (opModeIsActive())
        {

             // Send some stats to the telemetry

            telemetry.addData("System time nanos", System.nanoTime());
            telemetry.addData("FPS", String.format("%.2f", camera.getFps()));
            telemetry.addData("T Count", vision.getTargetCount());
            telemetry.addData("T X", vision.getTargetX());
            telemetry.update();

            sleep(5);
        }
    }

}