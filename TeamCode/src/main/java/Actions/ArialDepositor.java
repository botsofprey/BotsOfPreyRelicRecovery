package Actions;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import Autonomous.REVColorDistanceSensorController;
import MotorControllers.MotorController;

/**
 * Created by robotics on 12/2/17.
 */

/*
    A class to set up two motors to use our extendotron and belt for our glyph system
 */
public class ArialDepositor implements ActionHandler {
    SpoolMotor leftLiftMotor;
    SpoolMotor belt;
    REVColorDistanceSensorController glyphSensor;
    HardwareMap hardwareMap;
    double currentPosition = 0;
    final double UNKNOWN_DISTANCE = -1;
    private final static double FAST_RETRACT_SPEED = 10.0;
    private final static double FAST_EXTEND_SPEED = 15.0;
    private final static double SLOW_RETRACT_SPEED = 1.0;
    private final static double SLOW_EXTEND_SPEED = 5.0;

    public final static int LIFT_MOTOR = 4;
    public final static int GLYPH_MOTOR = 5;
    public final static int TICKS_PER_REV = 1120;
    public final static double EXTENDOTRON_DIAMETER_INCHES = 1.18;

    public enum GLYPH_PLACEMENT_LEVEL{GROUND,ROW1,ROW2,ROW3,ROW4};
    public final static double GROUND_LEVEL_PLACEMENT_HEIGHT = 0;
    public final static double ROW1_PLACEMENT_HEIGHT = 6;
    public final static double ROW2_PLACEMENT_HEIGHT = 12;
    public final static double ROW3_PLACEMENT_HEIGHT = 18;
    public final static double ROW4_PLACEMENT_HEIGHT = 24;
    GLYPH_PLACEMENT_LEVEL desiredLevel;

    public ArialDepositor(HardwareMap hw) throws Exception{
        hardwareMap = hw;
        leftLiftMotor = new SpoolMotor(new MotorController("liftMotor","MotorConfig/FunctionMotors/AerialLiftSpool.json", hardwareMap),FAST_EXTEND_SPEED,FAST_RETRACT_SPEED,100, hardwareMap);
        belt = new SpoolMotor(new MotorController("belt", "MotorConfig/FunctionMotors/BeltMotor.json", hardwareMap), 10, 10, 100, hardwareMap);
        belt.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        glyphSensor = new REVColorDistanceSensorController(REVColorDistanceSensorController.type.GLYPH_STACK_O_TRON, "glyphSensor", hardwareMap);
        //belt.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void extend(){
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setExtendSpeed(FAST_EXTEND_SPEED);
        leftLiftMotor.extend();
    }

    public void retract(){
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setExtendSpeed(FAST_RETRACT_SPEED);
        leftLiftMotor.retract();
    }

    public void slowRetract(){
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setExtendSpeed(SLOW_RETRACT_SPEED);
        leftLiftMotor.retract();
    }
    public void slowExtend(){
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLiftMotor.setExtendSpeed(SLOW_EXTEND_SPEED);
        leftLiftMotor.extend();
    }

    public void stopLift(){
        if(leftLiftMotor.getMotorControllerMode() == DcMotor.RunMode.RUN_USING_ENCODER || leftLiftMotor.getMotorControllerMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            leftLiftMotor.setPower(0);
            currentPosition = leftLiftMotor.getPosition();
            currentPosition = currentPosition/TICKS_PER_REV*(Math.PI*EXTENDOTRON_DIAMETER_INCHES);
            Log.d("Extendotron", "Update Position");
            Log.d("Extendotron", "Tick " + Long.toString(leftLiftMotor.getPosition()));
            Log.d("Extendotron", "Inch " + Double.toString(currentPosition));
        }
        goToLiftPosition(currentPosition);
    }

    public void goToLiftPosition(double positionInInches){
        leftLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLiftMotor.setPosition(positionInInches);
        leftLiftMotor.setPower(1);
    }


    public void goToGlyphLevel(GLYPH_PLACEMENT_LEVEL level){
        switch(level){
            case GROUND:
                goToLiftPosition(GROUND_LEVEL_PLACEMENT_HEIGHT);
                break;
            case ROW1:
                goToLiftPosition(ROW1_PLACEMENT_HEIGHT);
                break;
            case ROW2:
                goToLiftPosition(ROW2_PLACEMENT_HEIGHT);
                break;
            case ROW3:
                goToLiftPosition(ROW3_PLACEMENT_HEIGHT);
                break;
            case ROW4:
                goToLiftPosition(ROW4_PLACEMENT_HEIGHT);
                break;
        }
    }

    public void setLiftPower(double power){
        leftLiftMotor.setPower(power);
    }

    public void setLiftDirection(DcMotor.Direction dir){
        leftLiftMotor.setDirection(dir);
    }

    public void setBeltDirection(DcMotor.Direction dir){
        belt.setDirection(dir);
    }

    public void setBeltPower(double power){
        belt.setExtendPower(power);
    }

    public long getLiftMotorPosition(){
        return leftLiftMotor.getPosition();
    }

    public void startBelt(){
        belt.extendWithPower();
    }

    public void stopBelt(){
        belt.pause();
    }

    public void reverseBelt(){
        belt.retractWithPower();
    }

    public REVColorDistanceSensorController.color getColor() {
        return glyphSensor.getColor();
    }

    public double getDistance(DistanceUnit unit){
        return glyphSensor.getDistance(unit);
    }

    @Override
    public boolean doAction(String action, long maxTimeAllowed) {
        return false;
    }

    @Override
    public boolean stopAction(String action) {
        return false;
    }

    @Override
    public boolean startDoingAction(String action) {
        return false;
    }

    @Override
    public void stop() {
        leftLiftMotor.pause();
        leftLiftMotor.stop();
    }

}
