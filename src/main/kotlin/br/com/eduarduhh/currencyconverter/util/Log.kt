package br.com.eduarduhh.currencyconverter.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.log(): Logger = LoggerFactory.getLogger(T::class.java)
