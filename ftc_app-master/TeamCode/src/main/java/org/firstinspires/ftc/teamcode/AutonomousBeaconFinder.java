package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;

/**
 * Created by Willy Tilly Fettkether and Harrison Gieselman  on 11/15/2016.
 */

@Autonomous (name="Beacon Finder-Blue", group="Iterative Opmode")
public class AutonomousBeaconFinder extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    //Motor pointers
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;

    private DcMotor fork = null;
    private DcMotor shooter = null;

    int ticks = 1180;
    int RPM = 128;

    ColorSensor colorSensor;
    GyroSensor gyro;


    ModernRoboticsI2cRangeSensor front;
    ModernRoboticsI2cRangeSensor side;

    private Servo loader = null;
    float loaderPosition = 0;


    int stage = 0;

    double timeholder = 0;

    int position;



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

        //Initialize the motors and servos
        frontLeft  = hardwareMap.dcMotor.get("frontleft");
        frontRight = hardwareMap.dcMotor.get("frontright");
        rearLeft = hardwareMap.dcMotor.get("rearleft");
        rearRight = hardwareMap.dcMotor.get("rearright");

        fork = hardwareMap.dcMotor.get("fork");
        shooter = hardwareMap.dcMotor.get("shooter");

        rearRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        fork.setDirection(DcMotor.Direction.FORWARD);
        shooter.setDirection(DcMotor.Direction.REVERSE);

        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fork.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMaxSpeed(ticks * RPM);
        rearLeft.setMaxSpeed(ticks * RPM);
        frontRight.setMaxSpeed(ticks * RPM);
        rearRight.setMaxSpeed(ticks * RPM);

        colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(true);

        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.calibrate();


        //Range sensor initialization:

        front = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "front");
        side = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "side");

        loader = hardwareMap.servo.get("loader");

        loader.setPosition(loaderPosition);


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
        //A switch statement that decides which code block to load based on our current location in our routine
        switch (stage) {
            case 0: stage0();
                 break;
            case 1: stage1();
                break;
            case 2: stage2();
                break;
            case 3: stage3();
                break;
            case 4: stage4();
                break;
            case 5: stage5();
                break;
            case 6: stage6();
                break;
            case 7: stage65();
                break;
            case 8: stage7();
                break;
            case 9: stage8();
                break;
            case 10: stage9();
                break;
            case 11: stage10();
                break;
            case 12: stage11();
                break;
            case 13: stage12();
                break;
            case 14: stage13();
                break;
            case 15: stage14();
                break;
            case 16: stage15();
                break;
            case 17: stage16();
                break;

        }


    }

    //move 8 cm from the wall so our shooter doesn't snag
    public void stage0(){
        if(front.cmUltrasonic() < 8) {
            forward(-.25);
        } else {
            stopMoving();
            stage++;
        }
    }
    //shoot one particle
    public void stage1(){
        if(shooter.getCurrentPosition() < 4400){
            shooter.setPower(.5);
        } else {
            shooter.setPower(0);
            stage++;
            timeholder = runtime.milliseconds();
        }
    }
    //reload the shooter
    public void stage2(){
        loader.setPosition(.2);
        if(runtime.milliseconds() - timeholder >= 500){
            stage ++;
        }
    }
    //shoot second particle
    public void stage3(){
        if(shooter.getCurrentPosition() < 4400*2.37){
            shooter.setPower(.5);
        } else {
            shooter.setPower(0);
            stage++;
        }
    }
    //move forward to the cap ball
    public void stage4(){
        if(front.cmUltrasonic() <= 75 || front.cmUltrasonic() >= 230){
            forward(-.5);
        }else{
            stopMoving();
            stage++;
        }
    }
    //turn the robot 90 degrees
    public void stage5(){
        //if we overshoot our turn, go the other direction at a slower rate
        if(gyro.getHeading() > 300 || gyro.getHeading() < 50){
            turn(.4);
        }else if(gyro.getHeading() > 271 || gyro.getHeading() < 50){
            turn(.1);
        } else if(gyro.getHeading() < 269 && gyro.getHeading() > 50){
            turn(-.05);
        } else {
            stopMoving();
            stage++;
        }
    }
    //move forward towards the first beacon
    public void stage6(){
        if(!(front.cmUltrasonic() <= 48)){
            forward(.5);
        }else{
            stopMoving();
            stage++;
            position = frontLeft.getCurrentPosition();
        }
    }

    public void stage65(){
        if(Math.abs(frontLeft.getCurrentPosition()) - position < 300){
            strafe(-.2);
        }else{
            stopMoving();
            stage++;
        }
    }
    //strafe to line up
    public void stage7(){
        if(side.cmUltrasonic() > 200 ){
            strafe(-.2);
        }else{
            stopMoving();
            stage++;
        }
    }
    //finish moving forward
    public void stage8(){
        if(!(front.cmUltrasonic() <= 15)){
            forward(.2);
        }else{
            stopMoving();
            stage++;
        }
    }
    //make sure we are at 90 degrees
    public void stage9(){
        if(gyro.getHeading() > 300 || gyro.getHeading() < 50){
            turn(.4);
        }else if(gyro.getHeading() > 270 || gyro.getHeading() < 50){
            turn(.1);
        } else if(gyro.getHeading() < 270 && gyro.getHeading() > 50){
            turn(-.05);
        } else {
            stopMoving();
            stage++;
        }
    }
    //look for and press the beacon
    public void stage10() {
        if(!(front.cmUltrasonic() <= 12)){
            forward(.2);
        }
        if(colorSensor.blue() >= 2){  // nothing to stop it if it never sees <5
            if(!(front.cmUltrasonic() <= 5)){
                forward(.2);
            }else{
                stopMoving();
                stage++;
            }
        } else {
            strafe(-.2);  // should we have a stop moving?
        }
}
    //back up
    public void stage11(){
        if(!(front.cmUltrasonic() >= 20)){
            forward(-.2);
        }else{
            stopMoving();
            stage++;
        }
    }
    //strafe to the second beacon
    public void stage12(){
        if(front.cmUltrasonic() >= 28) {
            forward(0.2);
        }
        else if(front.cmUltrasonic() <= 18) {
            forward(-.2);
        }
        else if(side.cmUltrasonic() > 89 ){
            strafe(-.5);
        }else{
            stopMoving();
            stage++;
        }
    }
    //move forward
    public void stage13(){
        if(!(front.cmUltrasonic() <= 15)){
            forward(.2);
        }else{
            stopMoving();
            stage++;
        }
    }
    //turn to 90 degrees
    public void stage14(){
        if(gyro.getHeading() > 300 || gyro.getHeading() < 50){
            turn(.4);
        }else if(gyro.getHeading() > 270 || gyro.getHeading() < 50){
            turn(.1);
        } else if(gyro.getHeading() < 270 && gyro.getHeading() > 50){
            turn(-.05);
        } else {
            stopMoving();
            stage++;
        }
    }
    //look for and press the second beacon
    public void stage15(){
        if(!(front.cmUltrasonic() <= 12)){
            forward(.2);
        }
        if(colorSensor.blue() >= 2){
            if(!(front.cmUltrasonic() <= 5)){
                forward(.2);
            }else{
                stopMoving();
                stage++;
            }
        } else {
            strafe(-.2);
        }
    }
    //back up
    public void stage16(){
        if(!(front.cmUltrasonic() >= 20)){
            forward(-.2);
        }else{
            stopMoving();
            stage++;
        }
    }




    //functions for movement
    public void forward(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        rearLeft.setPower(power);
        rearRight.setPower(power);
    }

    public void turn(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        rearLeft.setPower(-power);
        rearRight.setPower(power);
    }

    public void strafe(double power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        rearLeft.setPower(power);
        rearRight.setPower(-power);
    }



    public void stopMoving() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        rearLeft.setPower(0);
        rearRight.setPower(0);
    }







}

