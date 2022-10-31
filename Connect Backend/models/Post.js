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
          date: {
            type: Date,
            default: Date.now(),
          },
          comment: {
            type: String,
          },
          

        },
        
      ],
    time:{
        type : Date,
        default : Date.now
    }
  }
);

module.exports = mongoose.model("post", postSchema);