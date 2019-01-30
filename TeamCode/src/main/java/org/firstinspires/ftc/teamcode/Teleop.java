package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name="TeleOp", group="FTCPio")
public class Teleop extends OpMode
{
    /* Declare OpMode members. */
    private HardwareInfinity robot = new HardwareInfinity();
    private static final double     TETRIX_TICKS_PER_REV    = 1440;
    private static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;
    private static final double     WHEEL_DIAMETER_CM   = 4.0*2.54 ;
    private static final double     COUNTS_PER_INCH         = (TETRIX_TICKS_PER_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_CM * 3.1415);
    double left;
    double right;
    double drive;
    double armB;
    double bar;
    double turn;
    double max;
    double arm;
    double pre_suq = 0;
    double pre_arm = 0;
    double pre_bar = 0;
    boolean armBAuto;
    boolean FBarAuto;
    //boolean flipster = false;
    //boolean flipster1 = false;
    boolean flipster2 = false;
    boolean deathFlip = false;
    float activate_suq = 0;
    int asuq = 0;

    // State used for updating telemetry;
    @Override
    public void init() {

        /*imu = hardwareMap.get(Gyroscope.class, "imu");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");*/
        //Acceleration gravity = imu.getGravity();

        robot.init(hardwareMap);

        telemetry.addData("Teleop", "Initiate");    //
        telemetry.update();
        robot.Camera.setPosition(0);
        robot.lunchBox.setPosition(HardwareInfinity.lunchBoxMAX_POSITION);
        robot.Latch.setPosition(HardwareInfinity.LatchMIN_POSITION);
        // Wait for the game to start (driver presses PLAY)
    }
    @Override
    public void start() {

    }
    @Override
    public void loop()
    {
        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        // This way it's also easy to just drive straight, or just turn.
        drive = gamepad1.left_stick_y;
        turn  =  -gamepad1.left_stick_x;
        armB = -gamepad2.left_stick_y;
        bar = gamepad2.right_stick_y;
        if (asuq==0) activate_suq = gamepad1.right_stick_y/4*(1+gamepad1.left_trigger);
        telemetry.addData("Succq:", gamepad1.right_stick_y/4 *(1+gamepad1.left_trigger));
        telemetry.addData("Succq Encoder: ", "%d", robot.Succq.getCurrentPosition());
        telemetry.addData("DT pos: ", robot.dropTop.getPosition());
        if (gamepad1.left_bumper) {
            arm = 1;
        } else if (gamepad1.right_bumper) {
            arm = -1;
        } else arm = 0;
        if (arm == 0) arm = gamepad2.left_trigger;
        if (arm == 0) arm = -gamepad2.right_trigger;
        //telemetry.addData("Trigger is", robot.trigger.isPressed() ? "Pressed" : "not Pressed");
        telemetry.addData("Bottom is", robot.botSwitch.getState() ? "Pressed" : "not Pressed");
        telemetry.addData("Top is", robot.topSwitch.getState() ? "Pressed" : "not Pressed");
        // Output the safe vales to the motor drives.
        if ((!robot.botSwitch.getState() && !robot.topSwitch.getState() && !robot.trigger.isPressed()))
        {
            robot.linearArm.setPower(-arm);
        }
        else if ((robot.botSwitch.getState() || robot.trigger.isPressed()) && arm < 0)
        {
            robot.linearArm.setPower(-arm);
        }
        else if ((robot.botSwitch.getState() || robot.trigger.isPressed()) && arm >= 0)
        {
            robot.linearArm.setPower(0);
        }
        else if (arm > 0)
        {
            robot.linearArm.setPower(-arm);
        }
        else if (robot.topSwitch.getState() && arm <= 0)
        {
            robot.linearArm.setPower(0);
        }

        //blended motion
        left  = (drive+turn)/2*(gamepad1.right_trigger*3/2+1)*0.75;
        right = (drive-turn)/2*(gamepad1.right_trigger*3/2+1)*0.75;
        telemetry.addData("Multiplier:", (gamepad1.right_trigger*3/2+1)*0.75);

        // Normalize the values so neither exceed +/- 1.0
        max = (Math.max(Math.abs(left), Math.abs(right)))/2;
        if (max > 1.0)
        {
            left /= max;
            right /= max;
        }
        if (gamepad1.y)
        {
            left *= -0.5*(1-gamepad1.right_trigger);
            right *= -0.5*(1-gamepad1.right_trigger);
            robot.motorLeft.setPower(right);
            robot.motorRight.setPower(left);
            telemetry.addData("Reverse","Activated");
        }
        else
        {
            robot.motorLeft.setPower(left);
            robot.motorRight.setPower(right);
            telemetry.addData("Reverse", "Deactivated");
        }

        /*if (gamepad1.right_bumper) {
            if (!flipster) {
                if (asuq == 0) {
                    asuq = 1;
                    activate_suq = asuq;
                    robot.Succq.setPower(activate_suq);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e){
                    }
                } else {
                    asuq = 0;
                }
                flipster = true;
            }
        } else {
            flipster = false;
        }*/
        if (gamepad2.left_bumper) robot.armBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        else robot.armBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (gamepad2.right_bumper) robot.FBar.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        else robot.FBar.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (gamepad2.a) {
            if (!flipster2) {
                if (!deathFlip) {
                    deathFlip = true;
                } else {
                    deathFlip = false;
                }
                flipster2 = true;
            }
        } else {
            flipster2 = false;
        }

        /*if (gamepad1.left_bumper) {
            if (!flipster1) {
                asuq = -asuq;
            }
            flipster1 = true;
        } else {
            flipster1 = false;
        }*/

        if (gamepad1.a) {
            robot.dropTop.setPosition(HardwareInfinity.DT_MAX);
        }
        //if the Succq isn't moving then stop it to save the motor
        if (asuq != 0) activate_suq = asuq;
        telemetry.addData("Death Flip: ", deathFlip);
        if ((activate_suq!=0) && (pre_suq == robot.Succq.getCurrentPosition()) && gamepad1.right_stick_y==0 && !deathFlip) {
            activate_suq = 0;
        }
        robot.Succq.setPower(activate_suq);
        pre_suq = robot.Succq.getCurrentPosition();
        if ((armB!=0) && (pre_arm == robot.armBase.getCurrentPosition()) && !deathFlip) {
            armB = 0;
        }
        telemetry.addData("Arm Base Power: ","%.5f",armB);
        telemetry.addData("Arm Base Encoder: ", "%d", robot.armBase.getCurrentPosition());
        if (!armBAuto) {
            robot.armBase.setPower(armB);
        } else if (armB!=0) {
            robot.armBase.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.armBase.setPower(0);
            armBAuto = false;
        }
        pre_arm = robot.armBase.getCurrentPosition();
        if ((bar!=0) && (pre_bar == robot.FBar.getCurrentPosition()) && !deathFlip) {
            bar = 0;
        }
        telemetry.addData("4Bar Power: ","%.5f",bar);
        telemetry.addData("4Bar Encoder: ", "%d", robot.FBar.getCurrentPosition());
        if (!FBarAuto) {
            robot.FBar.setPower(bar);
        } else if (bar!=0) {
            robot.FBar.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.FBar.setPower(0);
            armBAuto =false;
        }
        pre_bar = robot.Succq.getCurrentPosition();
        // Controls latching servos on linear actuator
        // Latch open
        if (gamepad1.dpad_right)
        {
            robot.Latch.setPosition(HardwareInfinity.LatchMAX_POSITION);
            telemetry.addData("Latches","Max");
        }
        // Latch closed
        if (gamepad1.dpad_left)
        {
            robot.Latch.setPosition(HardwareInfinity.LatchMIN_POSITION);
            telemetry.addData("Latches","Min");
        }
        if ((gamepad1.dpad_down || gamepad2.dpad_down) && robot.dropTop.getPosition()>HardwareInfinity.DT_MIN) {
            robot.dropTop.setPosition(robot.dropTop.getPosition()-0.015);
        }
        if ((gamepad1.dpad_up || gamepad2.dpad_up) && robot.dropTop.getPosition()<HardwareInfinity.DT_MAX) {
            robot.dropTop.setPosition(robot.dropTop.getPosition()+0.015);
        }
        if (gamepad2.b) {
            armBAuto = true;
            robot.armBase.setTargetPosition(-6000);
            robot.armBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armBase.setPower(-1);
            FBarAuto = true;
            robot.FBar.setTargetPosition(-11000);
            robot.FBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FBar.setPower(-1);
        }
        if (gamepad2.y) {
            armBAuto = true;
            robot.armBase.setTargetPosition(-300);
            robot.armBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armBase.setPower(1);
            FBarAuto = true;
            robot.FBar.setTargetPosition(-300);
            robot.FBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FBar.setPower(1);
        }
        if (gamepad2.x) {
            armBAuto = true;
            robot.armBase.setTargetPosition(-300);
            robot.armBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armBase.setPower(-1);
            FBarAuto = true;
            robot.FBar.setTargetPosition(-10000);
            robot.FBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FBar.setPower(-1);
        }
        if (robot.armBase.isBusy()) {
            armBAuto = true;
        } else if (armBAuto) {
            armBAuto = false;
            robot.FBar.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (robot.FBar.isBusy()) {
            FBarAuto = true;
        } else if (FBarAuto) {
            FBarAuto = false;
            robot.FBar.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
         // Drops team marker with servo
        if (gamepad1.x)
        {
            robot.lunchBox.setPosition(HardwareInfinity.lunchBoxMIN_POSITION);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            robot.lunchBox.setPosition(HardwareInfinity.lunchBoxMAX_POSITION);
            telemetry.addLine("Team Marker Dropped");
        }

        // Send telemetry message to signify robot running;
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("arm","%.2f", arm);
        telemetry.addData("Motor Encoder", "%d",robot.linearArm.getCurrentPosition());
        telemetry.update();
    }
}
