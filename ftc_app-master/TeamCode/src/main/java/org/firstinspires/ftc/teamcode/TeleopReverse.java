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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;                                                                                                                          
                                                                                                                                                                         
/*                                                                                                                                                                       
 * This is a test TeleOp mode                                                                                                                                            
 * Harrison Gieselman                                                                                                                                                    
 * Created 10/1/2016                                                                                                                                                     
 * Last Updated: 10/1/2016                                                                                                                                               
 */

@TeleOp(name="TeleOp Reverse", group="Iterative Opmode")  // @Autonomous(...) is the other common choice

public class TeleopReverse extends OpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    //Motor pointers                                                                                                                                                     
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;

    private DcMotor fork = null;
    private DcMotor shooter = null;

    private Servo loader = null;
    float loaderPosition = 0;

    //gamepad value holder variables                                                                                                                                     
    double leftY;
    double leftX;
    double rightY;
    double rightX;

    boolean setState = false;

    int ticks = 1180;
    int RPM = 128;



    /*                                                                                                                                                                   
     * Code to run ONCE when the driver hits INIT                                                                                                                        
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");                                                                                                                      
                                                                                                                                                                         
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters                                                                          
         * to 'get' must correspond to the names assigned during the robot configuration                                                                                 
         * step (using the FTC Robot Controller app on the phone).                                                                                                       
         */
        frontLeft  = hardwareMap.dcMotor.get("frontleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        rearLeft = hardwareMap.dcMotor.get("rearleft");
        rearRight = hardwareMap.dcMotor.get("rearright");

        fork = hardwareMap.dcMotor.get("fork");
        shooter = hardwareMap.dcMotor.get("shooter");


        rearRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        fork.setDirection(DcMotor.Direction.FORWARD);
        shooter.setDirection(DcMotor.Direction.REVERSE);

        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fork.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        frontLeft.setMaxSpeed(ticks * RPM);
        rearLeft.setMaxSpeed(ticks * RPM);
        frontRight.setMaxSpeed(ticks * RPM);
        rearRight.setMaxSpeed(ticks * RPM);

        fork.setMaxSpeed(ticks * RPM);
        shooter.setMaxSpeed(ticks * RPM);

        loader = hardwareMap.servo.get("loader");

        loader.setPosition(loaderPosition);

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);





        // eg: Set the drive motor directions:                                                                                                                           
        // Reverse the motor that runs backwards when connected directly to the battery                                                                                  
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors                                                                 
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors                                                                
        // telemetry.addData("Status", "Initialized");                                                                                                                   
    }

    /*                                                                                                                                                                   
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY                                                                                       
     */
    @Override
    public void init_loop() {
    }

    /*                                                                                                                                                                   
     * Code to run ONCE when the driver hits PLAY                                                                                                                        
     */
    @Override
    public void start() {
        runtime.reset();
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /*                                                                                                                                                                   
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP                                                                                        
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        leftY = gamepad1.left_stick_y;
        leftX = gamepad1.left_stick_x;
        rightX = gamepad1.right_stick_x;

        /*if(gamepad1.y) {
            setState = !setState;
        }*/
        if(setState == false) {
            frontLeft.setPower(leftY + leftX - rightX);
            rearLeft.setPower(leftY - leftX - rightX);
            rearRight.setPower(leftY + leftX + rightX);
            frontRight.setPower(leftY - leftX + rightX);
        } else {
            frontLeft.setPower(-leftY - leftX - rightX);
            rearLeft.setPower(-leftY + leftX - rightX);
            rearRight.setPower(-leftY - leftX + rightX);
            frontRight.setPower(-leftY + leftX + rightX);

        }

        if (gamepad1.x) {
            shooter.setPower(1);
        } else if (gamepad1.y) {
            shooter.setPower(-1);
        } else {
            shooter.setPower(0);
        }

        if (gamepad1.a) {
            loaderPosition += .01;
            if(loaderPosition > 1) {
                loaderPosition = 1;
            }
            loader.setPosition(loaderPosition);
        } else if(gamepad1.b) {
            loaderPosition -= .01;
            if(loaderPosition < 0) {
                loaderPosition = 0;
            }
            loader.setPosition(loaderPosition);
        }


        if(gamepad1.left_bumper) {
            fork.setPower(.5);
        } else if(gamepad1.right_bumper) {
            fork.setPower(-.5);
        } else if(!gamepad1.left_bumper && !gamepad1.right_bumper && gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0){
            fork.setPower(0);
        } else {
            fork.setPower(-.5*gamepad1.right_trigger + .5*gamepad1.left_trigger);
        }

        telemetry.addData("Servo Position:", loader.getPosition());
        telemetry.addData("Shooter Encoder Ticks: ", shooter.getCurrentPosition());


    }

    /*                                                                                                                                                                   
     * Code to run ONCE after the driver hits STOP                                                                                                                       
     */
    @Override
    public void stop() {
    }

}                                                                                                                                                                        
                                                                                                                                                                         