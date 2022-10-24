const mongoose= require('mongoose')

const userVerificationSchema = new mongoose.Schema({
    userID:{
        type: String
    },
    uniqueString: {
        type: String
    },
    createdAt: {
        type: Date,
        default: Date.now()
    }, 
    expiredAt: {
        type: Date,
        default: Date.now()+21000000
    }
})

module.exports= mongoose.model('UserVerification', userVerificationSchema);