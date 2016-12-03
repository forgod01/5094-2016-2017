package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
/**
 * Created by Willy Tilly Fettkether  on 11/15/2016.
 */

@Autonomous (name="Sensor Readings", group="Iterative Opmode")
public class SensorReadings extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    //Motor pointers
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;

    int ticks = 1180;
    int RPM = 128;

    ColorSensor color;
    GyroSensor gyro;

    //I2C values

    I2cAddr FRONTADDR = new I2cAddr(0x30);
    I2cAddr SIDEADDR = new I2cAddr(0x28);

    public static final int RANGE_REG_START = 0x04;
    public static final int RANGE_READ_LENGTH = 2;

    I2cDevice FRONT;
    I2cDevice SIDE;

    I2cDeviceSynch FRONTReader;
    I2cDeviceSynch SIDEReader;

    byte[] frontCache;
    byte[] sideCache;


    int stage = 0;



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

        rearRight.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        frontLeft.setMaxSpeed(ticks * RPM);
        rearLeft.setMaxSpeed(ticks * RPM);
        frontRight.setMaxSpeed(ticks * RPM);
        rearRight.setMaxSpeed(ticks * RPM);

        color = hardwareMap.colorSensor.get("color");
        color.enableLed(false);

        gyro = hardwareMap.gyroSensor.get("gyro");
        // start calibrating the gyro.
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();
        gyro.calibrate();

        // make sure the gyro is calibrated.

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();

        //I2C initialization:

        FRONT = hardwareMap.i2cDevice.get("front");
        SIDE = hardwareMap.i2cDevice.get("side");

        FRONTReader = new I2cDeviceSynchImpl(FRONT, FRONTADDR, false);
        SIDEReader = new I2cDeviceSynchImpl(SIDE, SIDEADDR, false);

        FRONTReader.engage();
        SIDEReader.engage();


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
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        frontCache = FRONTReader.read(RANGE_REG_START, RANGE_READ_LENGTH);
        sideCache = SIDEReader.read(RANGE_REG_START, RANGE_READ_LENGTH);

        telemetry.addData("Heading: ", gyro.getHeading());
        telemetry.addData("Side Ultrasonic: ", sideCache[0] & 0xFF);
        telemetry.addData("Side ODS: ", sideCache[1] & 0xFF);
        telemetry.addData("Front Ultrasonic: ", frontCache[0] & 0xFF);
        telemetry.addData("Side ODS: ", frontCache[1] & 0xFF);
        telemetry.addData("Red: ", color.red());
        telemetry.addData("Blue: ", color.blue());

        telemetry.update();
    }


    public void stage0(){

    }





    public void forward(float power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        rearLeft.setPower(power);
        rearRight.setPower(power);
    }

    public void turn(float power){
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        rearLeft.setPower(-power);
        rearRight.setPower(power);
    }

    public void strafe(float power){
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

