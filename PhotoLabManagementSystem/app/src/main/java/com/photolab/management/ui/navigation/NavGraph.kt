package com.photolab.management.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.photolab.management.ui.screens.customer.AddEditCustomerScreen
import com.photolab.management.ui.screens.customer.CustomerListScreen
import com.photolab.management.ui.screens.dashboard.DashboardScreen
import com.photolab.management.ui.screens.login.LoginScreen
import com.photolab.management.ui.screens.settings.SettingsScreen
import com.photolab.management.ui.screens.stock.StockScreen
import com.photolab.management.ui.screens.usermanagement.AddUserScreen
import com.photolab.management.ui.screens.usermanagement.UserManagementScreen

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val NEW_ORDER = "new_order"
    const val CUSTOMER_LIST = "customer_list"
    const val ADD_CUSTOMER = "add_customer"
    const val REPORTS = "reports"
    const val STOCK = "stock"
    const val SETTINGS = "settings"
    const val USER_MANAGEMENT = "user_management"
    const val ADD_USER = "add_user"
}

@Composable
fun PhotoLabNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNewOrder = { navController.navigate(Routes.NEW_ORDER) },
                onNewCustomer = { navController.navigate(Routes.CUSTOMER_LIST) },
                onViewReports = { navController.navigate(Routes.REPORTS) },
                onViewStock = { navController.navigate(Routes.STOCK) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onManageUsers = { navController.navigate(Routes.USER_MANAGEMENT) }
            )
        }

        composable(Routes.CUSTOMER_LIST) {
            CustomerListScreen(
                onBack = { navController.popBackStack() },
                onAddCustomer = { navController.navigate(Routes.ADD_CUSTOMER) },
                onCustomerClick = { /* Customer detail/edit screen — next build step */ }
            )
        }

        composable(Routes.ADD_CUSTOMER) {
            AddEditCustomerScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.STOCK) {
            StockScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.USER_MANAGEMENT) {
            UserManagementScreen(
                onBack = { navController.popBackStack() },
                onAddUser = { navController.navigate(Routes.ADD_USER) }
            )
        }

        composable(Routes.ADD_USER) {
            AddUserScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        // Placeholder destinations for modules still to come (Order, Reports/Sales).
        composable(Routes.NEW_ORDER) { /* Order module — next build step */ }
        composable(Routes.REPORTS) { /* Reports module — next build step */ }
    }
}
