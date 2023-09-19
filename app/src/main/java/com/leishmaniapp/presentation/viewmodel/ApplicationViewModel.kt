package com.leishmaniapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leishmaniapp.entities.disease.Disease
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    /**
     * Currently selected disease
     */
    var disease: Disease? = null;
}