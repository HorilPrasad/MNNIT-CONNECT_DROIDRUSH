const mongoose= require('mongoose')

const userSchema = new mongoose.Schema({
    name:{
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    email: {
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    password: {
        type: String,
        required: true,
        max: 1024,
        min: 6
    }, 
    date: {
        type: Date,
        default: Date.now()
    },
    verified: {
        type: Boolean,
        default: true
    },
    location: {
        type: String
    },
    dob:{
        type: String
    },
    gender: {
        type: String,
        enum: ['Male', 'Female', 'Other']
    },
    token: {
        type: String,
    },
    phone: {
        type: String
    },
    regNo:{
        type: String
    },
    branch: {
        type: String
    },
    imageUrl: {
        type: String
    },
    role:{
        type:String,
        default:"user"
    }

})

module.exports=mongoose.model('User', userSchema);
