package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
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
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.Servo;

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

    float position = 0;

    Servo loader = null;

    ColorSensor colorSensorBot;
    ColorSensor colorSensor;
    GyroSensor gyro;


    ModernRoboticsI2cRangeSensor front;
    ModernRoboticsI2cRangeSensor side;


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
       /* frontLeft  = hardwareMap.dcMotor.get("frontleft");
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
        rearRight.setMaxSpeed(ticks * RPM);*/


        colorSensor = hardwareMap.colorSensor.get("color");
        colorSensor.enableLed(true);

        colorSensorBot = hardwareMap.colorSensor.get("color_bot");
        colorSensorBot.enableLed(true);

        gyro = hardwareMap.gyroSensor.get("gyro");

        gyro.calibrate();


        //Range sensor initialization:

        front = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "front");
        side = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "side");


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


        telemetry.addData("Heading: ", gyro.getHeading());
        telemetry.addData("Side Ultrasonic: ", side.cmUltrasonic());
        telemetry.addData("Side ODS: ", side.cmOptical());
        telemetry.addData("Front Ultrasonic: ", front.cmUltrasonic());
        telemetry.addData("Front ODS: ", front.cmOptical());
        telemetry.addData("Red: ", colorSensor.red());
        telemetry.addData("Blue: ", colorSensor.blue());
        telemetry.addData("Bottom Red: ", colorSensorBot.red());
        telemetry.addData("Bottom Blue: ", colorSensorBot.blue());

        telemetry.update();
    }

}

