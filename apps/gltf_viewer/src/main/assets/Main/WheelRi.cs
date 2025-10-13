using Godot;

public partial class WheelRi : RigidBody3D
{
	[Export] public float InitialSpeed = 5f;
	// Node để lấy kết quả sau khi bánh xe dừng
	[Export] public WheelResult ResultNode;

	[Signal]
	public delegate void WheelStopped(string voucherCode);

	// Biến trạng thái để theo dõi xem bánh xe có đang quay hay không
	private bool _isRolling = false;

	public override void _Ready()
	{
		//Test
		StartRolling(); 
	}

	/// <summary>
	/// Bắt đầu quá trình quay bánh xe.
	/// </summary>
	public void StartRolling()
	{
		if (_isRolling)
		{
			GD.Print("Bánh xe đã đang quay!");
			return;
		}

		GD.Print($"Bắt đầu quay bánh xe với tốc độ: {InitialSpeed}");
		// Áp dụng một mô-men xoắn lớn để bánh xe bắt đầu quay
		ApplyTorque(new Vector3(0, 0, InitialSpeed * 200f));
		_isRolling = true;
	}

	public override void _Process(double delta)
	{
		// Chỉ kiểm tra khi bánh xe đang ở trạng thái quay
		if (!_isRolling)
		{
			return;
		}

		// Kiểm tra xem vận tốc góc có đủ nhỏ không (bánh xe đã dừng lại)
		if (AngularVelocity.LengthSquared() < 0.01f)
		{
			WheelPiece result = ResultNode.GetResult();
			if (result != null)
			{
				GD.Print($"Kết quả là {result.PieceIndex} với voucher {result.VoucherCode}");
				// --- PHÁT SIGNAL ---
				// Phát signal và truyền theo kết quả
				EmitSignal(nameof(WheelStopped), result.VoucherCode);
			}
			else
			{
				GD.PrintErr("Không thể lấy kết quả từ ResultNode!");
			}
			_isRolling = false;
		}
	}
}
