package Autonomous.OpModes;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import Autonomous.VuforiaHelper;
import java.util.ArrayList;

import Autonomous.ImageProcessing.CryptoBoxColumnImageProcessor;

/**
 * Created by root on 11/20/17.
 */

/*
    An opmode to test saving images using vuforia
 */
@Autonomous(name="Save Image Test", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class VuforiaImageCaptureTest extends LinearOpMode{

    CryptoBoxColumnImageProcessor cryptoFinder;
    final int IMAGE_PROCESS_WIDTH = 171;
    final int IMAGE_PROCESS_HEIGHT = 224;
    @Override
    public void runOpMode() throws InterruptedException {
        int imageTaken = 0;
        CryptoBoxColumnImageProcessor.CRYPTOBOX_COLOR color = CryptoBoxColumnImageProcessor.CRYPTOBOX_COLOR.BLUE;
        /*To access the image: you need to iterate through the images of the frame object:*/
        VuforiaHelper vuforia = new VuforiaHelper();telemetry.update();while (!opModeIsActive()) {
            if (gamepad1.start) {
                if(color == CryptoBoxColumnImageProcessor.CRYPTOBOX_COLOR.BLUE) color = CryptoBoxColumnImageProcessor.CRYPTOBOX_COLOR.RED;
                else color = CryptoBoxColumnImageProcessor.CRYPTOBOX_COLOR.BLUE;
                while (gamepad1.start) ;
            }
            telemetry.addData("Color", color);
            telemetry.update();
        }
        cryptoFinder = new CryptoBoxColumnImageProcessor(CryptoBoxColumnImageProcessor.DESIRED_HEIGHT,CryptoBoxColumnImageProcessor.DESIRED_WIDTH,.1,1, color);
        telemetry.addData("Status","Initialized");

        waitForStart();
        Bitmap bmp;
        ArrayList<Integer> columnLocations = new ArrayList<Integer>();
        while(opModeIsActive()){
            long timeStart = System.currentTimeMillis();
            bmp = vuforia.getImage(CryptoBoxColumnImageProcessor.DESIRED_WIDTH,CryptoBoxColumnImageProcessor.DESIRED_HEIGHT);
            if(bmp != null){
                long algorithmStart = System.currentTimeMillis();
                columnLocations = cryptoFinder.findColumns(bmp,true);
                telemetry.addData("Algorithm Time", "" + (System.currentTimeMillis() - algorithmStart));
                if(columnLocations != null){
                    for(int i = 0; i < columnLocations.size(); i ++){
                        telemetry.addData("Column " + i, " " + columnLocations.get(i).intValue());
                    }
                }
                if(imageTaken == 0) {
                    vuforia.saveBMP(bmp); // save edited image
                    imageTaken++;
                }
                telemetry.addData("Loop Time", "" + (System.currentTimeMillis() - timeStart));
                telemetry.update();
            }
            else{
                Log.d("BMP","NULL!");
            }
        }

    }







}
