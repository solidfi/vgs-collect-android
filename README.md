# VGSCollect-android

## Table of contents
- [Overview](#overview)
- [Getting started](#getting-started)
- [Parameters](#parameters)
- [Tooling](#tooling)
- [API Reference](#api-reference)
- [Packages](#third-party-libraries)


## Overview
Using VGSCollect sdk we can set a pin for the cards

## Getting started
Configure VGSCollect Sample :
- Clone repository
```groovy
git clone git@github.com:solidfi/vgs-collect-android.git
OR
git clone https://github.com/solidfi/vgs-collect-android.git
```
- Open it in [Android Studio](https://developer.android.com/studio)
- Run the project and test it out.

## Parameters

In order to start the project we need below parameters. All fields are mandatory

//For Set PIN

- VGS Vault ID : Talk to our solutions team (solutions@solidfi.com) to get the ID via a secured method. (It will different base on sandbox and live)
- Card ID : Id of your card
- Debit Card Token : You will get it from the "pintoken" api from the backend
- Environment : Select live or sandbox environment
- Last 4 digit of card : Last 4 digit of card number
- Expiry Month : Expiry month (MM) of the card
- Expiry Year : Expiry year (YYYY) of the card
- 4 Digit Pin : Pin you want to set for the card (It should be numeric)

Notes: "Debit Card Token" can be used only once to set pin. You need to call "pintoken" api to generate new "Debit Card Token"

//For Link Card

- VGS Vault ID : Talk to our solutions team (solutions@solidfi.com) to get the ID via a secured method. (It will different base on sandbox and live)
- Contact ID : Id of the contact
- Debit Card Token : You will get it from the "debittoken" api from the backend
- Environment : Select live or sandbox environment
- Card Number : Last 4 digit of card number
- Exp Month : Expiry month (MM) of the card
- Exp Year : Expiry year (YYYY) of the card
- Address : Address of the card (used static in the code)

Notes: "Debit Card Token" can be used only once to link card. You need to call "debittoken" api to generate new "Debit Card Token"

## Tooling
- Android 5.0 +
- Android Studio Android Studio Arctic Fox
- Kotlin 1.6.0

## API Reference
- VGS Collect Integration : https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk/integration
- Solid API Integration : https://documenter.getpostman.com/view/13543869/TWDfEDwX#ce8c0e57-0dcf-45ea-87d8-6f03a302e027

## Packages
- VGSCollectSDK
