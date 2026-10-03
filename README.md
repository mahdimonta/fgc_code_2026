# 2026 Robot – TeleOp Code

Java TeleOp OpMode for our **FIRST Global Challenge (FGC) 2026** robot, written for the FTC SDK.

- **OpMode name:** `2026 robot`
- **Class:** `MotorOpMode` (`org.firstinspires.ftc.teamcode`)
- **Type:** iterative `OpMode` (`init()` runs once, `loop()` runs repeatedly)

## Hardware

| Mechanism | Hardware type | Config name(s) |
|-----------|---------------|----------------|
| Drivetrain (2 motors) | `DcMotor` | `leftMotor`, `rightMotor` |
| Intake | `DcMotor` | `intake` |
| Shooter (2 motors) | `DcMotor` | `shooterLeft`, `shooterRight` |
| Ramp / door (2 servos) | `Servo` | `leftServo`, `rightServo` |
| Climber | `DcMotor` | `climber` |

These names must match the Robot Configuration on the Driver Station.

## Controls

### Gamepad 1 (driver)

| Input | Action |
|-------|--------|
| Right stick Y | Drive forward / backward |
| Left stick X | Turn left / right |
| Right trigger | Climber up |
| Left trigger | Climber down |

### Gamepad 2 (operator)

| Input | Action |
|-------|--------|
| Right trigger | Intake in |
| Left trigger | Intake out (reverse) |
| Right bumper | Toggle shooter (forward, full power) |
| Left bumper | Toggle shooter (reverse, full power) |
| A | Toggle ramp/door open / closed (only while the shooter is running) |

## How it works

### Drivetrain
Arcade drive. Forward/backward comes from the right stick and turning from the left stick. The two values are combined for each side (`left = drive + turn`, `right = drive - turn`) and clipped to the range -1 to 1. The left motor is reversed in `init()` so both sides push the robot the same way.

### Intake
Right trigger minus left trigger on gamepad 2 gives the intake power, so one trigger pulls in and the other pushes out.

### Shooter
The two bumpers are toggles, and only one direction can be active at a time. Turning on one direction turns off the other. Pressing the active bumper again stops the shooter. Both shooter motors run at full power.

### Ramp / door
The two servos open to position `0.6`. When closed they sit at about `0.95` (left) and `0.9` (right). The right servo is reversed in `init()`. The door can only be opened while the shooter is running. When the shooter turns off, a 5 second timer starts, and if the door is still open when it ends, it closes automatically.

### Climber
Gamepad 1 triggers control the climber (right = up, left = down). Brake mode is on, so it holds its position when the triggers are released.

### Telemetry
The Driver Station shows drive and turn values, intake power, shooter status, door status, servo positions, and climber power.

## Setup

1. Copy `MotorOpMode.java` to `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.
2. Set up the hardware configuration with the names above.
3. Build and deploy to the Robot Controller.
4. On the Driver Station, choose **TeleOp → 2026 robot**, then press INIT and PLAY.

## Tuning

- If the robot drives backward, flip the sign of `drive`. If it turns the wrong way, flip the sign of `turn`.
- Servo open/closed positions are set directly in `loop()` and can be adjusted there.
