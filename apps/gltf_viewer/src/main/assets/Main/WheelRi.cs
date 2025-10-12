using Godot;
public partial class WheelRi : RigidBody3D
{
	[Export] byte Speed=5;
	[Export] WheelResult ResultNode;
	public event System.Action OnWheelStopped;
	public override void _Ready()
	{
		//--test--
		RollWheel(Speed*200f);
	}
	internal void RollWheel(float speed)
	{
		GD.Print("RollWheel speed=" + speed);
		ApplyTorque(new Vector3(0, 0, speed));
		hasPrinted=true;
	}
	bool hasPrinted = false;
	public override void _Process(double delta)
	{
		if(!hasPrinted)
			return;
		if(AngularVelocity.LengthSquared()<0.01f)
		{
			var result = ResultNode.GetResult();
			if(result!=null)
			{
				GD.Print($"Result is {result.PieceIndex} with voucher {result.VoucherCode}");
				hasPrinted=false;
				// QueueFree();
			}
		}
	}
}
