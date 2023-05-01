import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
fun Button(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    MaterialButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text)
    }
}

@Composable
fun Switch(
    onChanged: (Boolean) -> Unit,
    checked: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaterialSwitch(
            onCheckedChange = onChanged,
            checked = checked,
        )

        Text(text = text)
    }
}