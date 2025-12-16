package k23cnt2.nqdproject3.service;

import jakarta.servlet.http.HttpSession;
import k23cnt2.nqdproject3.entity.NqdProduct;
import k23cnt2.nqdproject3.model.NqdCartItemDto;
import k23cnt2.nqdproject3.repository.NqdProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NqdCartService {

    private static final String CART_SESSION_KEY = "NQD_CART";

    private final NqdProductRepository productRepository;

    @SuppressWarnings("unchecked")
    private Map<Long, NqdCartItemDto> getCartMap(HttpSession session) {
        Object data = session.getAttribute(CART_SESSION_KEY);
        if (data instanceof Map) {
            return (Map<Long, NqdCartItemDto>) data;
        }
        Map<Long, NqdCartItemDto> map = new LinkedHashMap<>();
        session.setAttribute(CART_SESSION_KEY, map);
        return map;
    }

    // ====== thêm vào giỏ ======
    public void addToCart(Long productId, int quantity, HttpSession session) {
        if (quantity < 1) quantity = 1;

        Map<Long, NqdCartItemDto> cart = getCartMap(session);
        NqdCartItemDto item = cart.get(productId);

        if (item == null) {
            NqdProduct p = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            item = new NqdCartItemDto(
                    p.getId(),
                    p.getName(),
                    p.getImageUrl(),
                    p.getPrice(),
                    quantity
            );
            cart.put(productId, item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
    }

    // ====== thay đổi số lượng (±1) ======
    public void changeQuantity(Long productId, int change, HttpSession session) {
        Map<Long, NqdCartItemDto> cart = getCartMap(session);
        NqdCartItemDto item = cart.get(productId);
        if (item == null) return;

        int newQty = item.getQuantity() + change;
        if (newQty <= 0) {
            cart.remove(productId);
        } else {
            item.setQuantity(newQty);
        }
    }

    // ====== xóa 1 sản phẩm ======
    public void removeItem(Long productId, HttpSession session) {
        Map<Long, NqdCartItemDto> cart = getCartMap(session);
        cart.remove(productId);
    }

    public List<NqdCartItemDto> getCartItems(HttpSession session) {
        return new ArrayList<>(getCartMap(session).values());
    }

    public BigDecimal getCartTotal(HttpSession session) {
        return getCartItems(session).stream()
                .map(NqdCartItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartCount(HttpSession session) {
        return getCartItems(session).stream()
                .mapToInt(NqdCartItemDto::getQuantity)
                .sum();
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
