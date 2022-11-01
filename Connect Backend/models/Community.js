const mongoose = require('mongoose')

const communitySchema = new mongoose.Schema({   
    userId:{
        type : String,
        required:true
    },
    name: {
        type: String,
        required: true
    },
    about: {
        type: String
        , required: true
    },
    tag: {
        type: String,
        default: "mnnit"
    },
    rules: {
        type: String,
        default: "Act properly"
    },
    image: {
        type: String,
        default: ""

    },
    admins:[{
        user:{
            type: String,
            require: true
        }
    }
    ]
});

module.exports = mongoose.module('community', communitySchema);
