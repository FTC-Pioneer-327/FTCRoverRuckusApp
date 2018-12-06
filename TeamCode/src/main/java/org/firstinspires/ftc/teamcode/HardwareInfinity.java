package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
//import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Motor channel:  Left  drive motor:        "motorLeft"
 * Motor channel:  Right drive motor:        "motorRight"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareInfinity
{
    /* Public OpMode members. */
    DcMotor  motorLeft;
    DcMotor  motorRight;
    DcMotor  linearArm;
    DcMotor Succq;
    Servo lunchBox;
    Servo Latch;
    Servo Camera;
    Servo Collector;
    DigitalChannel botSwitch;
    DigitalChannel topSwitch;
    TouchSensor trigger;

    //public Servo    rightClaw   = null;

    static final double lunchBoxMAX_POSITION = 0.35;
    static final double lunchBoxMIN_POSITION = 0.875;
    static final double LatchMAX_POSITION = 0.87;
    static final double LatchMIN_POSITION = 0;

    /* local OpMode members. */
    HardwareMap hwMap;
    //private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    HardwareInfinity(){

    }

    /* Initialize standard Hardware interfaces */
    void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        motorLeft  = hwMap.get(DcMotor.class, "motorLeft");
        motorRight = hwMap.get(DcMotor.class, "motorRight");
        linearArm  = hwMap.get(DcMotor.class, "linearArm");
        Succq = hwMap.get(DcMotor.class, "succq");
        linearArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeft.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        motorRight.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        botSwitch = hwMap.get(DigitalChannel.class, "botSwitch");
        topSwitch = hwMap.get(DigitalChannel.class, "topSwitch");
        trigger = hwMap.get(TouchSensor.class, "trigger");

        // Set all motors to zero power
        motorLeft.setPower(0);
        motorRight.setPower(0);
        linearArm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        linearArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Succq.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        lunchBox  = hwMap.get(Servo.class, "lunchBox");
        Latch  = hwMap.get(Servo.class, "Latch");
        Camera = hwMap.get(Servo.class, "Camera");
        Collector = hwMap.get(Servo.class, "collector");


        lunchBox.setPosition(lunchBoxMAX_POSITION);
        Latch.setPosition(LatchMAX_POSITION);
        Collector.setPosition(0.6);
        Camera.setPosition(0.5);

    }
}