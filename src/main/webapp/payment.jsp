<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán học phí</title>
    <style>
        .payment-container { display: flex; flex-direction: column; align-items: center; padding: 40px; }
        .qr-section { background: #fff; padding: 40px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); width: 450px; text-align: center; }
        .qr-image { width: 100%; max-width: 300px; border-radius: 8px; border: 1px solid #eee; margin: 20px 0; }
        .transfer-info { text-align: left; background: #f8fafc; padding: 15px; border-radius: 8px; margin-bottom: 25px; line-height: 1.8; border: 1px dashed #cbd5e1;}
        .btn-confirm { background: #10B981; color: #fff; padding: 14px 20px; border: none; border-radius: 8px; cursor: pointer; width: 100%; font-size: 16px; font-weight: bold; transition: 0.3s;}
        .btn-confirm:hover { background: #059669; }
        .warning-text { font-size: 13px; color: #64748b; margin-top: 15px; font-style: italic;}
    </style>
</head>
<body style="background: #f5f7fa;">

    <div class="payment-container">
        <div class="qr-section">
            <h2>Thanh toán học phí</h2>
            <p style="color: #64748b;">Mở App ngân hàng để quét mã QR</p>
            
            <img src="${qrUrl}" alt="Mã QR Thanh Toán" class="qr-image"/>
            
            <div class="transfer-info">
                <p><strong>Ngân hàng:</strong> MB Bank</p>
                <p><strong>Số tài khoản:</strong> 090123456789</p>
                <p><strong>Chủ tài khoản:</strong> TRUNG TAM NGOAI NGU LMCS</p>
                <p><strong>Số tiền:</strong> <span style="color: red; font-weight: bold; font-size: 18px;">${amount} VNĐ</span></p>
                <p><strong>Nội dung CK:</strong> <span style="font-weight: bold; color: #2563eb;">${addInfo}</span></p>
            </div>

            <form action="payment" method="post">
                <input type="hidden" name="action" value="confirmPayment">
                <input type="hidden" name="enrollmentId" value="${enrollmentId}">
                <input type="hidden" name="amount" value="${amount}">
                
                <button type="submit" class="btn-confirm">TÔI ĐÃ CHUYỂN KHOẢN THÀNH CÔNG</button>
            </form>
            
            <p class="warning-text">* Lưu ý: Chỉ bấm nút sau khi tiền đã bị trừ khỏi tài khoản của bạn. Bộ phận học vụ sẽ đối chiếu tự động dựa trên nội dung chuyển khoản.</p>
        </div>
    </div>

</body>
</html>