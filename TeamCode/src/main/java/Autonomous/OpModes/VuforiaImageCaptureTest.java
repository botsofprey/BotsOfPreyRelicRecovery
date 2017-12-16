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

@Autonomous(name="Save Image Test", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class VuforiaImageCaptureTest extends LinearOpMode{

    CryptoBoxColumnImageProcessor cryptoFinder;
    final int IMAGE_PROCESS_WIDTH = 171;
    final int IMAGE_PROCESS_HEIGHT = 224;
    @Override
    public void runOpMode() throws InterruptedException {

        /*To access the image: you need to iterate through the images of the frame object:*/
        VuforiaHelper vuforia = new VuforiaHelper();
        cryptoFinder = new CryptoBoxColumnImageProcessor(171,244,.1,1);
        telemetry.addData("Status","Initialized");
        telemetry.update();
        waitForStart();
        Bitmap bmp;
        ArrayList<Integer> columnLocations = new ArrayList<Integer>();
        while(opModeIsActive()){
            bmp = vuforia.getImage();
            if(bmp != null){
                //vuforia.saveBMP(bmp);
                columnLocations = cryptoFinder.findColumns(bmp,false);
                if(columnLocations != null){
                    for(int i = 0; i < columnLocations.size(); i ++){
                        telemetry.addData("Column " + i, " " + columnLocations.get(i).intValue());
                    }
                    telemetry.update();

                }
            }
            else{
                Log.d("BMP","NULL!");
            }
        }

    }







}
