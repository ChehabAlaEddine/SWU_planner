package com.example.swu_planner.features.routing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.swu_planner.data.model.Stop

/**
 * Dialog for setting a new address (Home, Work, or Custom).
 */
@Composable
fun AddressSettingDialog(
    uiState: RoutingUiState,
    onSettingNameChanged: (String) -> Unit,
    onSettingIconChanged: (String) -> Unit,
    onAddressSettingQueryChanged: (String) -> Unit,
    onAddressSettingStopSelected: (Stop) -> Unit,
    onSaveNewAddress: () -> Unit,
    onCancelAddressSetting: () -> Unit
) {
    val draft = uiState.addressDraft
    val type = draft.type ?: return

    AlertDialog(
        onDismissRequest = onCancelAddressSetting,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = {
            val title = if (type == "other") "Add Address"
            else "Set ${type.replaceFirstChar { it.uppercase() }} Stop"
            Text(title)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (type == "other") {
                    OutlinedTextField(
                        value = draft.name,
                        onValueChange = onSettingNameChanged,
                        label = { Text("Name (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Select Icon:", style = MaterialTheme.typography.labelMedium)
                    IconPicker(
                        selectedIcon = draft.icon,
                        onIconSelected = onSettingIconChanged
                    )
                }
                
                AutocompleteField(
                    label = "Search Stop / Address",
                    value = draft.query,
                    onValueChange = onAddressSettingQueryChanged,
                    hints = draft.hints,
                    onHintSelected = onAddressSettingStopSelected
                )
            }
        },
        confirmButton = {
            Row {
                TextButton(onClick = onCancelAddressSetting) {
                    Text("Cancel")
                }
                if (type == "other") {
                    Button(
                        onClick = onSaveNewAddress,
                        enabled = draft.latitude != null && draft.query.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    )
}

/**
 * Dialog for editing an existing saved address.
 */
@Composable
fun AddressEditDialog(
    uiState: RoutingUiState,
    onEditNameChanged: (String) -> Unit,
    onEditIconChanged: (String) -> Unit,
    onAddressSettingQueryChanged: (String) -> Unit,
    onAddressSettingStopSelected: (Stop) -> Unit,
    onSaveEditedAddress: () -> Unit,
    onDeleteEditingAddress: () -> Unit,
    onCancelEditingAddress: () -> Unit
) {
    val draft = uiState.addressDraft
    val address = draft.editingAddress ?: return
    
    AlertDialog(
        onDismissRequest = onCancelEditingAddress,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = { Text("Edit Address") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = draft.name,
                    onValueChange = onEditNameChanged,
                    label = { Text("Name (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (address.type.startsWith("other")) {
                    Text("Select Icon:", style = MaterialTheme.typography.labelMedium)
                    IconPicker(
                        selectedIcon = draft.icon,
                        onIconSelected = onEditIconChanged
                    )
                }

                AutocompleteField(
                    label = "Search Stop / Address",
                    value = draft.query,
                    onValueChange = onAddressSettingQueryChanged,
                    hints = draft.hints,
                    onHintSelected = onAddressSettingStopSelected
                )

                Text(
                    text = "Current Address: ${address.address}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onDeleteEditingAddress) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
                Row {
                    TextButton(onClick = onCancelEditingAddress) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onSaveEditedAddress,
                        enabled = draft.query.isNotBlank() && (draft.editingAddress?.latitude != 0.0)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    )
}

/**
 * A horizontal row of icons for users to select from when setting a custom address.
 *
 * @param selectedIcon The name of the currently selected icon.
 * @param onIconSelected Callback invoked when an icon is clicked.
 */
@Composable
fun IconPicker(
    selectedIcon: String,
    onIconSelected: (String) -> Unit
) {
    val icons = listOf(
        "place" to Icons.Default.Place,
        "school" to Icons.Default.School,
        "star" to Icons.Default.Star,
        "favorite" to Icons.Default.Favorite,
        "gym" to Icons.Default.FitnessCenter,
        "work" to Icons.Default.Work,
        "home" to Icons.Default.Home
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        icons.forEach { (name, icon) ->
            IconButton(
                onClick = { onIconSelected(name) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (selectedIcon == name) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                )
            ) {
                Icon(icon, contentDescription = name)
            }
        }
    }
}
