package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//As of 9.15.19.0706 this serves only to test sensor input

@Autonomous(name = "WTest", group = "Sensor")
public class WallTrackTesting extends LinearOpMode {
    HardwareInfinity1 hwInf = new HardwareInfinity1();

    public SensorTriplet sensors = new SensorTriplet();

    public static class SensorTriplet {

        DistanceSensor[] distanceSensors = new DistanceSensor[3];

        enum TripletType {
            Right,
            Center,
            Left
        }

        //<editor-fold desc="Inits">
        //dL = the left most sensor
        //dC = the center sensor
        //dR = the right most sensor
        public SensorTriplet(DistanceSensor dL, DistanceSensor dC, DistanceSensor dR) {
            distanceSensors[0] = dL;
            distanceSensors[1] = dC;
            distanceSensors[2] = dR;
        }

        public SensorTriplet(OpMode opMode, String dL, String dC, String dR) {
            distanceSensors[0] = opMode.hardwareMap.get(DistanceSensor.class, dL);
            distanceSensors[1] = opMode.hardwareMap.get(DistanceSensor.class, dC);
            distanceSensors[2] = opMode.hardwareMap.get(DistanceSensor.class, dR);
        }

        public SensorTriplet() {
        }
        //</editor-fold>

        //<editor-fold desc="External return groups">
        public double getDistance(TripletType type, DistanceUnit unit) {
            return sensor(type).getDistance(unit);
        }

        //Return a distance sensor from type
        public DistanceSensor sensor(TripletType type) {
            return type == TripletType.Center ? distanceSensors[1] : (type == TripletType.Right ? distanceSensors[2] : distanceSensors[0]);
        }
        //</editor-fold>
    }

    @Override
    public void runOpMode() throws InterruptedException {
        hwInf.init(hardwareMap, this);
        sensors = new SensorTriplet(this, "SENSOR_NAME_LEFT", "SENSOR_NAME_MID", "SENSOR_NAME_RIGHT");

        while (opModeIsActive()) {
            telemetry.addData("Right sensor data : ", sensors.getDistance(SensorTriplet.TripletType.Right, DistanceUnit.CM));
            telemetry.addData("Left sensor data : ", sensors.getDistance(SensorTriplet.TripletType.Left, DistanceUnit.CM));
            telemetry.addData("Mid sensor data : ", sensors.getDistance(SensorTriplet.TripletType.Center, DistanceUnit.CM));

           double wallAngle = ((bMath.pi() * 3) / 4) - Math.atan(sensors.getDistance(SensorTriplet.TripletType.Right, DistanceUnit.CM) / sensors.getDistance(SensorTriplet.TripletType.Left, DistanceUnit.CM));
            telemetry.addData("Wall angle : ", wallAngle);
            telemetry.update();

        }

    }
}
