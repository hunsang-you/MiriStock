import './css/BuySell.css';
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userStore } from '../store';
import { stockAPI } from '../api/api';
import { AiOutlineInfoCircle } from 'react-icons/ai';
import Swal from 'sweetalert2';

const UpdateBuy = () => {
  const navigate = useNavigate();
  console.log('updatebuy');
  return <div>123123</div>;
};

export default UpdateBuy;
