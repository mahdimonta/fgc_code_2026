package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name= "2026 robot")
public class MotorOpMode extends OpMode {
    
    DcMotor left,right,intake,shooterLeft,shooterRight,climber;
    Servo leftServo,rightServo;
    Float joyRY,joyLX,joyRX,intakeTriggerL,intakeTriggerR,climberTR,climberTL;
    Double leftP,rightP;
    Boolean shooterStatus = false,
    lastShooterStatus = false,
    lastShooterStatusR=false,
    shooterStatusR=false,
    rampStatus=false,
    lastRampstatus=false,
    doorTimer = false,
    shooterOn = false,
    lastShooterOn = false;
    
    ElapsedTime shooterOffTimer = new ElapsedTime();
    @Override
    public void init(){
        
    
        //drivetrain
        left = hardwareMap.get(DcMotor.class, "leftMotor");
        right = hardwareMap.get(DcMotor.class, "rightMotor");
        left.setDirection(DcMotor.Direction.REVERSE);
    
    
    
        //intake
        intake = hardwareMap.get(DcMotor.class, "intake");
        
        //shooter
        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        
        //ramp 
        
        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.REVERSE);


        //climber
        climber = hardwareMap.get(DcMotor.class, "climber");
        climber.setDirection(DcMotor.Direction.REVERSE);
        climber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        
        
        
        

        
    }
    @Override
    public void loop(){
        
        
        
        //drivetrain
        
        joyRY = gamepad1.right_stick_y;
        joyRX = gamepad1.right_stick_x;
        
        joyLX = gamepad1.left_stick_x;
       
        telemetry.addData("right Y stick", joyRY);
        telemetry.addData("right X stick", joyRX);
        telemetry.addData("left X stick", joyLX);
        

        
        
        left.setPower(joyRY);
        right.setPower(joyRY);
        
        
        left.setPower(-joyLX);
        right.setPower(joyLX);
        
        //intake
        
        intakeTriggerL = gamepad2.left_trigger;
        intakeTriggerR = gamepad2.right_trigger;
        
        telemetry.addData("intake val", intakeTriggerR - intakeTriggerL);
        
        intake.setPower(intakeTriggerR - intakeTriggerL);
        
        
        //shooter
        if (gamepad2.right_bumper && !lastShooterStatus ){
            shooterStatus = !shooterStatus;
            shooterStatusR =false;
        }else if(gamepad2.left_bumper && !lastShooterStatusR){
            shooterStatusR = !shooterStatusR;
            shooterStatus = false;
        }
        lastShooterStatus = gamepad2.right_bumper;
        lastShooterStatusR = gamepad2.left_bumper;
        
        
        
        shooterOn = shooterStatus || shooterStatusR;


        if (lastShooterOn && !shooterOn) {
            shooterOffTimer.reset();
            doorTimer = true;
        }

        lastShooterOn = shooterOn;
        
        
        
        if(shooterStatus){
            shooterLeft.setPower(1.0);
            shooterRight.setPower(1.0);
        }else if(shooterStatusR){
            shooterLeft.setPower(-1.0);
            shooterRight.setPower(-1.0);
        }else{
            shooterLeft.setPower(0);
            shooterRight.setPower(0);
        }
         
       
        
       
        telemetry.addData("shooter status", shooterStatus);
        telemetry.addData("shooter reverse status", shooterStatusR);
        
        //ramp
        
        
        leftP = leftServo.getPosition();
        rightP = rightServo.getPosition();
        
        if (gamepad2.a && !lastRampstatus ){
            if(lastShooterOn && shooterOn){
                rampStatus = !rampStatus;
            }
        }
        lastRampstatus = gamepad2.a;
        if(rampStatus){
            if(lastShooterOn && shooterOn){
                leftServo.setPosition(0.6);
                rightServo.setPosition(0.6);  
            }
            
        }else{
            leftServo.setPosition(1-0.05);
            rightServo.setPosition(1-0.1);
        }
        

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
        
        //climber

        climberTL = gamepad1.left_trigger;
        climberTR = gamepad1.right_trigger;
        
        climber.setPower(climberTR - climberTL);

        
        telemetry.addData("intake val", climberTR - climberTL);
        
        
        telemetry.update();
        
    }
    
    
}




