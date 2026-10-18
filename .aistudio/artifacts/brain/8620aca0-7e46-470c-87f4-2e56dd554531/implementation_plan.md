# Cuelinks Affiliate Routing & Direct Redirection Refactoring Plan

Refactor outbound affiliate link conversion, remove debug dialogs, ensure robust URI encoding to prevent Amazon/Flipkart error pages, and integrate production Cuelinks publisher credentials from environment secrets.

## User Review & Critical Decisions

> [!IMPORTANT]
> - **Direct Redirection**: Tapping "Buy Now" or store buttons will instantly launch the store URL or native app without any intermediate modal.
> - **URI Encoding**: All target URLs are fully double/single URI-encoded so e-commerce apps handle them correctly without 404 or "Something went wrong" errors.
> - **Production Publisher ID**: Utilizes environment publisher credentials (`CUELINKS_API_KEY` / Publisher ID).

- **Confirmed Decision**: Direct deep linking / browser redirection without modal confirmation.
- **Confirmed Decision**: Instant linksredirect format for zero latency.

---

### 1. Overview & Core Concept
- **What It Does**: Streamlines the affiliate checkout flow by removing intermediate debug popups and correctly formatting URI-encoded Cuelinks URLs (`https://linksredirect.com/?cid=<PUBLISHER_ID>&url=<ENCODED_URL>`).
- **Key Value**: Guarantees seamless monetization and error-free product page navigation across Amazon, Flipkart, and Myntra.

### 2. User Experience & Visual Design
- **Interaction Flow**: User taps "Compare & Buy" -> Background Cuelinks URL conversion with correct encoding -> Instant launch via `Intent.ACTION_VIEW`.
- **Loading / Feedback**: Clean and immediate transition.

### 3. Key Product Decisions & Technical Strategy
- **URI Encoding**: Use `URLEncoder.encode(url, "UTF-8")` correctly so query params in Amazon/Flipkart links remain intact.
- **Publisher Credentials**: Read from `BuildConfig.CUELINKS_API_KEY` or fallback environment variables.
- **Removal of Debug Popups**: Remove `CuelinksRedirectDialog` triggers and dialog state from `CompareCartViewModel` and UI screens.

### 4. Technical Architecture & Component Hierarchy

```
[User Tap: Buy Now]
       │
       ▼
[CompareCartViewModel / Manager]
       │
       ├─► Read Publisher ID from BuildConfig / Secrets
       ├─► URI Encode Target URL
       └─► Generate Cuelinks URL (linksredirect.com/?cid=ID&url=...)
       │
       ▼
[Intent.ACTION_VIEW Launch] ──► [Native App / Browser (Amazon / Flipkart)]
```
