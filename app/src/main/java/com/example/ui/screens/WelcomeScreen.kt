package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserSessionManager
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    sessionManager: UserSessionManager,
    onContinueToHome: () -> Unit
) {
    var selectedAuthMode by remember { mutableStateOf<String?>(null) } // "PHONE", "GOOGLE", "EMAIL", null
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var emailAddress by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundWhite,
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // App Brand Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(listOf(AmazonNavy, AmazonTeal))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "CompareCart",
                        tint = AmazonOrange,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "CompareCart",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = AmazonNavy,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "India's Multi-Store Price Comparison & Deal Intelligence",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMutedGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Feature Highlights Banner
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceLightGrey,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureHighlightRow(
                            icon = Icons.Default.Storefront,
                            title = "Real-Time 8+ Store Comparison",
                            description = "Live prices across Amazon, Flipkart, JioMart, Myntra, Tata CLiQ & Croma."
                        )
                        FeatureHighlightRow(
                            icon = Icons.Default.Verified,
                            title = "Asli Discount Verification",
                            description = "90-day tracking detects fake price markups and pre-sale inflation."
                        )
                        FeatureHighlightRow(
                            icon = Icons.Default.CreditCard,
                            title = "Bank Card Savings Engine",
                            description = "Instant discounts for HDFC, SBI, ICICI & Axis cards computed live."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Error message banner if any
                if (errorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(errorMessage!!, color = Color(0xFF991B1B), fontSize = 12.sp)
                        }
                    }
                }

                // 3 PRIMARY LOGIN OPTIONS

                // 1. Phone OTP Option
                AnimatedVisibility(visible = selectedAuthMode != "PHONE") {
                    Button(
                        onClick = {
                            selectedAuthMode = "PHONE"
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_phone_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmazonNavy)
                    ) {
                        Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Login with Phone Number (OTP)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                // Phone OTP Interactive Form
                AnimatedVisibility(visible = selectedAuthMode == "PHONE") {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceWhite,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(AmazonTeal)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (!otpSent) "Enter Mobile Number" else "Verify 4-Digit OTP",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                IconButton(onClick = { selectedAuthMode = null; otpSent = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedGrey)
                                }
                            }

                            if (!otpSent) {
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { if (it.length <= 10) phoneNumber = it.filter { ch -> ch.isDigit() } },
                                    label = { Text("Mobile Number (10 Digits)") },
                                    placeholder = { Text("9876543210") },
                                    prefix = { Text("+91 ", fontWeight = FontWeight.Bold, color = TextDark) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("phone_input"),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        if (phoneNumber.length == 10) {
                                            otpSent = true
                                            otpCode = "4826" // Auto-filled for convenient 1-tap test OTP
                                            errorMessage = null
                                        } else {
                                            errorMessage = "Please enter a valid 10-digit mobile number."
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("send_otp_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = AmazonTeal),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Get OTP", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            } else {
                                Text(
                                    text = "OTP sent to +91 $phoneNumber. Test code: 4826",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AsliGreen,
                                    fontWeight = FontWeight.Medium
                                )

                                OutlinedTextField(
                                    value = otpCode,
                                    onValueChange = { if (it.length <= 6) otpCode = it.filter { ch -> ch.isDigit() } },
                                    label = { Text("Enter OTP Code") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("otp_input"),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        if (otpCode.length >= 4) {
                                            isProcessing = true
                                            sessionManager.loginWithPhone(phoneNumber)
                                            onContinueToHome()
                                        } else {
                                            errorMessage = "Enter the 4-digit verification code."
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("verify_otp_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = AsliGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    if (isProcessing) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                    } else {
                                        Text("Verify & Continue", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Google / Email Option
                AnimatedVisibility(visible = selectedAuthMode != "GOOGLE") {
                    OutlinedButton(
                        onClick = {
                            selectedAuthMode = "GOOGLE"
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_google_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceWhite,
                            contentColor = TextDark
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                        )
                    ) {
                        // Google "G" Badge Icon
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4285F4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("G", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Continue with Google or Email",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark
                        )
                    }
                }

                // Google / Email Interactive Form
                AnimatedVisibility(visible = selectedAuthMode == "GOOGLE") {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceWhite,
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(AmazonTeal)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Google or Email Sign In",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                IconButton(onClick = { selectedAuthMode = null }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMutedGrey)
                                }
                            }

                            // 1-Tap Google Quick Sign-In Simulation
                            Button(
                                onClick = {
                                    isProcessing = true
                                    sessionManager.loginWithGoogle(
                                        email = "shopper@gmail.com",
                                        name = "Rahul Sharma"
                                    )
                                    onContinueToHome()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("quick_google_signin"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Sign In with Google Account", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
                                Text(" or enter email ", color = TextLightGrey, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 4.dp))
                                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
                            }

                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Your Name (Optional)") },
                                placeholder = { Text("Rahul") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = emailAddress,
                                onValueChange = { emailAddress = it },
                                label = { Text("Email Address") },
                                placeholder = { Text("you@example.com") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("email_input"),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    if (emailAddress.contains("@") && emailAddress.contains(".")) {
                                        isProcessing = true
                                        sessionManager.loginWithEmail(emailAddress, fullName)
                                        onContinueToHome()
                                    } else {
                                        errorMessage = "Please enter a valid email address."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_email_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = AmazonNavy),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Continue with Email", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 3. Continue as Guest / Skip for Now (Prominent & Instant)
                TextButton(
                    onClick = {
                        sessionManager.continueAsGuest()
                        onContinueToHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("continue_guest_button")
                ) {
                    Text(
                        text = "Continue as Guest / Skip for Now",
                        fontWeight = FontWeight.Bold,
                        color = AmazonTeal,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = AmazonTeal,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Footer note
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "By continuing, you agree to CompareCart Terms of Service & Privacy Policy.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextLightGrey,
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun FeatureHighlightRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(AsliGreenContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AsliGreen,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                fontSize = 13.sp
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextMutedGrey,
                fontSize = 11.sp
            )
        }
    }
}
