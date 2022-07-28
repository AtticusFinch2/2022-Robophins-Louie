package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.TimestampedOpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;


import java.util.ArrayList;
import java.util.List;

public class FindYellowTarget extends TimestampedOpenCvPipeline
{
    Point textAnchor;
    Scalar green = new Scalar(0,255,0,255);

    int TargetCount = 0;
    int TargetX = 0;

    @Override
    public void init(Mat mat)
    {
        textAnchor = new Point(20, (mat.height()) - 20);
    }

    @Override
    public Mat processFrame(Mat input, long captureTimeNanos)
    {
        Rect foundRect = new Rect(); // Found rect
        Mat dst;
        dst = input.clone();
        Scalar value = new Scalar(255, 0, 0);
        int borderType = Core.BORDER_CONSTANT;
        int top, bottom, left, right;
        top = (int) (0.05*input.rows()); bottom = top;
        left = (int) (0.05*input.cols()); right = left;
        Point tp = new Point (110, 115);
        Point bt = new Point (300, 300);
        Rect bestRect = foundRect;
        int tester = 0;

        Imgproc.GaussianBlur(input, dst, new Size(251, 251), 0, 0);

        //mask the image
        Imgproc.cvtColor(input,dst,Imgproc.COLOR_BGR2HSV);
        Scalar low= new Scalar(90,150,150);
        Scalar high= new Scalar(115,255,255);
        Mat mask = new Mat();
        Core.inRange(dst,low,high,mask);
        int contourcount = 0;

        //Find the contours in the feed
        Mat srcGray = new Mat();
        Mat cannyOutput = new Mat();
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        //look for the actual contours in the pixels
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //create a bounding rectangle around the biggest target
        double maxVal = 0;
        int maxValIdx = 0;
        TargetCount = contours.size();
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
        {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            if (maxVal < contourArea)
            {
                maxVal = contourArea;
                maxValIdx = contourIdx;
                Imgproc.boundingRect(contours.get(contourIdx));
            }
        }

        //calculate the coordinates of the moment/contour itself and draw a green circle around largest target

        if (contours.size() > 0) {

            Moments p = Imgproc.moments(contours.get(maxValIdx));
            int x = (int) (p.get_m10() / p.get_m00());
            int y = (int) (p.get_m01() / p.get_m00());
            this.TargetX = x;
            Imgproc.circle(dst, new Point(x, y), 10, new Scalar(0, 255, 0), 4);
        }

        //draw the actual contours themselves after finding them
        Imgproc.drawContours(dst, contours, maxValIdx, new Scalar(245,0,0), 5);
        //int area1 = Imgproc.contourArea(contours, true);

        //put some text on the image
        //Imgproc.putText(dst, String.format("Louie's Capture time: %d", captureTimeNanos), textAnchor, Imgproc.FONT_HERSHEY_PLAIN, 1.5, green, 2);
        Imgproc.putText(dst, String.format("Contour X: "+ TargetX), textAnchor, Imgproc.FONT_HERSHEY_SIMPLEX, 1, green, 2);
        //telemetry.addData("TARGET X", x);
        //telemetry.update();
        /*telemetry.addData("TARGET Y", y);
        telemetry.update();*/

        return dst;
    }

    public int getTargetCount (){
        return TargetCount;
    }

    public int getTargetX (){
        return TargetX;
    }




}