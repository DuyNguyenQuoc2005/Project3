package k23cnt2.nqdproject3.entity;

public enum NqdOrderStatus {
    PENDING,        // Đã đặt, chờ xác nhận
    PROCESSING,     // Đang xử lý / chuẩn bị bánh
    SHIPPING,       // Đang giao
    COMPLETED,      // Đã giao xong
    CANCELED        // Đã hủy
}
