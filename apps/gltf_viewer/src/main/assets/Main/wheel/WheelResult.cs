using Godot;
public partial class WheelResult : Node3D
{
	[Export] WheelPiece[] Pieces ;
	public override void _Ready()
	{
		parentBody = GetParent<RigidBody3D>();
		var child=GetChildren();
		Pieces=new WheelPiece[child.Count];
		foreach(var c in child)
		{
			var C= c as WheelPiece;
			Pieces[c.GetIndex()]=C ;	
			C.GetChild<Label3D>(0).Text+=$"{C.VoucherCode}";
		}
	}
	[Export] Node3D Pointer;
	internal WheelPiece GetResult()
	{
		float minDistance = float.MaxValue;
		WheelPiece result = null;
		for (int i = 0,length=Pieces.Length; i < length; i++)
		{
			var piece = Pieces[i];
			var distance=(piece.GlobalPosition-Pointer.GlobalPosition).LengthSquared();
			if (distance < minDistance)
			{
				minDistance = distance;
				result = piece;
			}
		}
		return result;
	}
}
