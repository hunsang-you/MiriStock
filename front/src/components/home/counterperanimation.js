import { animate } from 'framer-motion';
import React, { useEffect, useRef } from 'react';
import './css/countanimation.css';

function CounterPer({ from, to }) {
  const nodeRef = useRef();
  useEffect(() => {
    const node = nodeRef.current;

    const controls = animate(from, to, {
      duration: 1,
      onUpdate(value) {
        node.textContent = value.toFixed(2) + '%';
      },
    });

    return () => controls.stop();
  }, [from, to]);

  return <span ref={nodeRef}></span>;
}

export default CounterPer;
