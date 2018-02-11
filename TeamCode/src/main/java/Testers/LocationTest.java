/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package Testers;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import Actions.JewelJousterV2;
import Autonomous.Location;
import DriveEngine.JennyNavigation;
import UserControlled.JoystickHandler;

import static Autonomous.RelicRecoveryField.BLUE_ALLIANCE_2;
import static Autonomous.RelicRecoveryField.startLocations;
import static DriveEngine.JennyNavigation.HIGH_SPEED_IN_PER_SEC;
import static DriveEngine.JennyNavigation.MED_SPEED_IN_PER_SEC;
import static DriveEngine.JennyNavigation.NORTH;

/*
    An opmode to test if all our drive wheels are working correctly
 */
@TeleOp(name="Zero Joust Tester", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class LocationTest extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    JennyNavigation navigation;
    JoystickHandler rightJoystick, leftJoystick;

    @Override
    public void runOpMode() {
        double turnRps = 0;
        try {
            navigation = new JennyNavigation(hardwareMap, startLocations[BLUE_ALLIANCE_2], NORTH, "RobotConfig/JennyV2.json");
            rightJoystick = new JoystickHandler(gamepad1, JoystickHandler.RIGHT_JOYSTICK);
            leftJoystick = new JoystickHandler(gamepad1, JoystickHandler.LEFT_JOYSTICK);
        }
        catch (Exception e){
            Log.e("Error!" , "Glyph Lift: " + e.toString());
            throw new RuntimeException("Glyph Lift Creation Error! " + e.toString());

        }
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if(gamepad1.a){
                navigation.driveToLocation(new Location(0, 0), MED_SPEED_IN_PER_SEC, this);
                telemetry.addData("Target Location", "(0, 0)");
            }
            else if(gamepad1.b) {
                navigation.driveToLocation(new Location(12, 12), MED_SPEED_IN_PER_SEC, this);
                telemetry.addData("Target Location", "(12, 12)");
            }
            else if(gamepad1.x){
                navigation.driveToLocation(new Location(-12, 12), MED_SPEED_IN_PER_SEC, this);
                telemetry.addData("Target Location", "(-12, 12)");
            }
            else if(gamepad1.y){
                navigation.driveToLocation(new Location(0, 24), MED_SPEED_IN_PER_SEC, this);
                telemetry.addData("Target Location", "(0, 24)");
            }
            else {
                turnRps = .7 *rightJoystick.magnitude() * rightJoystick.x()/Math.abs(rightJoystick.x());
                navigation.relativeDriveOnHeadingWithTurning(leftJoystick.angle(), HIGH_SPEED_IN_PER_SEC * leftJoystick.magnitude(), turnRps);
            }
            telemetry.addData("Current Location", "(" + navigation.getRobotLocation().getX() + ", " + navigation.getRobotLocation().getY() + ")");
            telemetry.update();
        }
    }
}