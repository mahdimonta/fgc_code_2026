package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "2026 robot")
public class MotorOpMode extends OpMode {

    DcMotor left, right, intake, shooterLeft, shooterRight, climber;
    Servo leftServo, rightServo;

    double drive, turn, leftPower, rightPower;
    double leftP, rightP;

    boolean shooterStatus = false,
            lastShooterStatus = false,
            shooterStatusR = false,
            lastShooterStatusR = false,
            rampStatus = false,
            lastRampStatus = false,
            doorTimer = false,
            shooterOn = false,
            lastShooterOn = false;

    ElapsedTime shooterOffTimer = new ElapsedTime();

    @Override
    public void init() {

        // drivetrain
        left = hardwareMap.get(DcMotor.class, "leftMotor");
        right = hardwareMap.get(DcMotor.class, "rightMotor");
        left.setDirection(DcMotor.Direction.REVERSE);

        // intake
        intake = hardwareMap.get(DcMotor.class, "intake");

        // shooter
        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        // ramp
        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.REVERSE);

        // climber
        climber = hardwareMap.get(DcMotor.class, "climber");
        climber.setDirection(DcMotor.Direction.REVERSE);
        climber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {

        // ---------------- drivetrain (arcade drive) ----------------
        // Stick Y is negative when pushed up, so it is inverted.
        drive = -gamepad1.right_stick_y;
        turn = gamepad1.left_stick_x;

        leftPower = drive + turn;
        rightPower = drive - turn;

        // Keep power within [-1, 1]
        leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
        rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        telemetry.addData("drive", drive);
        telemetry.addData("turn", turn);

        // ---------------- intake ----------------
        double intakePower = gamepad2.right_trigger - gamepad2.left_trigger;
        intake.setPower(intakePower);
        telemetry.addData("intake val", intakePower);

        // ---------------- shooter ----------------
        if (gamepad2.right_bumper && !lastShooterStatus) {
            shooterStatus = !shooterStatus;
            shooterStatusR = false;
        } else if (gamepad2.left_bumper && !lastShooterStatusR) {
            shooterStatusR = !shooterStatusR;
            shooterStatus = false;
        }
        lastShooterStatus = gamepad2.right_bumper;
        lastShooterStatusR = gamepad2.left_bumper;

        shooterOn = shooterStatus || shooterStatusR;

        // Shooter just turned off: start the door auto-close timer
        if (lastShooterOn && !shooterOn) {
            shooterOffTimer.reset();
            doorTimer = true;
        }
        lastShooterOn = shooterOn;
        if (shooterStatus) {
            shooterLeft.setPower(1.0);
            shooterRight.setPower(1.0);
        } else if (shooterStatusR) {
            shooterLeft.setPower(-1.0);
            shooterRight.setPower(-1.0);
        } else {
            shooterLeft.setPower(0);
            shooterRight.setPower(0);
        }
        telemetry.addData("shooter status", shooterStatus);
        telemetry.addData("shooter reverse status", shooterStatusR);
        // ---------------- ramp / door ----------------
        leftP = leftServo.getPosition();
        rightP = rightServo.getPosition();
        // Toggle the ramp with A (only while the shooter is running)
        if (gamepad2.a && !lastRampStatus) {
            if (shooterOn) {
                rampStatus = !rampStatus;
            }
        }
        lastRampStatus = gamepad2.a;
        if (rampStatus) {
            if (shooterOn) {
                leftServo.setPosition(0.6);
                rightServo.setPosition(0.6);
            }
        } else {
            leftServo.setPosition(1 - 0.05);
            rightServo.setPosition(1 - 0.1);
        }
        // Auto-close the door 5 seconds after the shooter turns off
        if (!shooterOn &&
                rampStatus &&
                doorTimer &&
                shooterOffTimer.seconds() >= 5.0) {

            leftServo.setPosition(0.95);
            rightServo.setPosition(0.9);
            rampStatus = false;
            doorTimer = false;
        }
        telemetry.addData("door status", rampStatus);
        telemetry.addData("Servo Left Position", leftP);
        telemetry.addData("Servo Right Position", rightP);

        // ---------------- climber ----------------
        double climberPower = gamepad1.right_trigger - gamepad1.left_trigger;
        climber.setPower(climberPower);
        telemetry.addData("climber val", climberPower);
        telemetry.update();
    }
}
