const mongoose = require("mongoose");

// DB Schema
const postSchema = new mongoose.Schema(
  {
    userId: {
        type: String,
        required: true
    },
    info: {
        type:String,
        max:500
    },
    likes: {
        type: Array,
        default: []
    },
    image: {
        type: String
    },

    communityName:{
      type:String
    },

    communityId:{
      type:String
    },
    dislikes:{
        type: Array,
        default:[]
    },
    comments: [
        {
          user: {
            type: String,
            required:true
          },
          time: {
            type: String ,
           required : true
          },
          comment: {
            type: String,
          },
          

        },
        
      ],
    time:{
        type : String,
        required : true
    
    }
  }
);

module.exports = mongoose.model("post", postSchema);