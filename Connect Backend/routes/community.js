const Community = require('../models/Community');
const express = require('express')
const router = express.Router();

//create community Post method

router.post('/create',async(req,res,next)=>{

    const community =new Community(req.body);
    try{
        await community.save();
        res.status(200).send({
            status:200,
            message:"community created"
        })
    }catch(err){
        next(err);
    }
})

router.get('/')

module.exports = router;