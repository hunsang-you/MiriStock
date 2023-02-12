import { createTheme } from '@mui/material/styles';
//Mui Custon Color Setting
export const theme = createTheme({
  palette: {
    primary: {
      main: '#6DCEF5',
      light: '#6DCEF5',
      // light: main값을 통해 계산됨
      // dark: main값을 통해 계산됨
      // contrastText: main값을 통해 계산됨
    },
    red: {
      main: '#D2143C',
      light: '#D2143C',
    },
    blue: {
      main: '#1E5DFF',
      light: '#1E5DFF',
    },
    grey: {
      main: '#8B8B8B',
      light: '#8B8B8B',
    },
  },
});
