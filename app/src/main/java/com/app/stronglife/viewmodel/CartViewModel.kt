import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.stronglife.data.model.CartItem
import com.app.stronglife.data.model.Product

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: State<List<CartItem>> = _cartItems

    fun addProduct(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }

        if (index != -1) {
            val updatedItem = current[index].copy(quantity = current[index].quantity + 1)
            current[index] = updatedItem
        } else {
            current.add(CartItem(product, quantity = 1))
        }

        _cartItems.value = current
    }

    fun decreaseProduct(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }

        if (index != -1) {
            val item = current[index]
            if (item.quantity > 1) {
                current[index] = item.copy(quantity = item.quantity - 1)
            } else {
                current.removeAt(index)
            }
            _cartItems.value = current
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
