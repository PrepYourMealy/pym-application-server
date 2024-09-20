package app.prepmymealy.application.service

import org.springframework.stereotype.Service

@Service
class UserPromptValidationService {

    fun validateUserPrompt(prompt: String): Boolean {
        return prompt.isNotEmpty() && prompt.length <= 200 // TODO make this a property
    }
}